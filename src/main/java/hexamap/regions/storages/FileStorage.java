/*
 * Copyright (c) 2021, Jérôme Brongniart
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the copyright holder nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software without 
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package hexamap.regions.storages;

import hexamap.coordinates.Coordinate;
import hexamap.regions.Region;
import hexamap.storage.indexators.Indexator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Iterator;

/**
 *
 * @param <Data> some stuff
 */
public class FileStorage<Data extends Externalizable> extends AbstractIndexatorStorage<Data> {

    private final FileChannel channel;
    private final int datatBytesSize;
    private final Data zero;

    private final ByteBuffer cache;
    private int cacheIndex;
    private final int PAGE_SIZE = 1024;

    public FileStorage(Region region, Indexator indexator, Class<Data> dataClass) throws Exception {
        super(region, indexator);

        File f = Files.createTempFile("test-", ".hexamap").toFile();
        this.channel = new RandomAccessFile(f, "rw").getChannel();

        zero = dataClass.getConstructor().newInstance();
        datatBytesSize = toByteArray(zero).length;

        cache = ByteBuffer.wrap(new byte[PAGE_SIZE * datatBytesSize]);
        cacheIndex = -1;

        initZero();
    }

    @Override
    protected Data indexGet(int index) {
        if (cacheIndex == index/PAGE_SIZE) {
            try {
                return readFromCache(index);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            cache.clear();
            while (cache.hasRemaining()) {
                assert channel.read(cache, (long) ((index / PAGE_SIZE) * datatBytesSize)) != -1;
            }
            cacheIndex = index/PAGE_SIZE;
            return readFromCache(index);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected Data indexPut(int index, Data data) {
        Data old = indexGet(index);
        try {
            writeToCache(index, data);
            cache.position(0);
            while (cache.hasRemaining()) {
                channel.write(cache, (long) ((index / PAGE_SIZE) * datatBytesSize));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return old;
    }

    @Override
    public void indexClear() {
        try {
            initZero();
        } catch (Exception e) {
        }
        cacheIndex = -1;
    }

    @Override
    public Iterator<Entry<Coordinate, Data>> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void writeToCache(int index, Data data) throws Exception {
        byte[] buff = toByteArray(data);
        cache.position((index % PAGE_SIZE) * datatBytesSize);
        cache.put(buff, 0, datatBytesSize);
    }

    private Data readFromCache(int index) throws Exception {
        byte[] buff = new byte[datatBytesSize];
        cache.position((index % PAGE_SIZE) * datatBytesSize);
        cache.get(buff, 0, datatBytesSize);
        return fromByteArray(buff);
    }

    private byte[] toByteArray(Data data) throws Exception {
        byte[] result;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(data);
            oos.flush();
            result = bos.toByteArray();
            assert datatBytesSize == 0 || result.length == datatBytesSize;
        }
        return result;
    }

    private Data fromByteArray(byte[] bytes) throws Exception {
        assert bytes.length == datatBytesSize;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream iis = new ObjectInputStream(bis)) {
            Data result = (Data) iis.readObject();
            return result;
        }
    }

    private void initZero() throws Exception {
        byte[] zeroBytes = toByteArray(zero);
        for (int i = 0; i < PAGE_SIZE; i++) {
            cache.put(zeroBytes);
        }
        int loop = region.size() / PAGE_SIZE;
        if (region.size() % PAGE_SIZE > 0) {
            loop++;
        }
        assert loop > 0;
        channel.position(0);
        for (int i = 0; i < loop; i++) {
            cache.position(0);
            channel.write(cache);
        }
        channel.force(true);
    }
}
