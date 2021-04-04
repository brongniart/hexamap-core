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
import hexamap.regions.indexators.Indexator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Iterator;

/**
 *
 * @param <Data> some stuff
 */
public class FileStorage<Data extends Externalizable > extends AbstractIndexatorStorage<Data> {

    private final FileChannel channel;
    private final int objSize;
    private final Data zero;
    private final Class<Data> dataClass;

    public FileStorage(Region region, Indexator indexator, Class<Data> dataClass) throws Exception {
        super(region, indexator);
        this.dataClass = dataClass;

        File f = Files.createTempFile("test-", ".hexamap").toFile();
        this.channel = new RandomAccessFile(f, "rw").getChannel();

        zero = dataClass.getConstructor().newInstance();
        objSize = toByteArray(zero).length;
        System.out.println("hexamap.regions.storages.FileStorage.<init>() " + objSize);
        initZero();
        System.out.println("hexamap.regions.storages.FileStorage.<init>() - End");
    }

    @Override
    protected Data indexGet(int index) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(objSize);

            int nbRead = 0;
            while (buffer.hasRemaining()) {
                nbRead += channel.read(buffer, (long) (index * objSize));
            }
            return fromByteArray(buffer.array());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected Data indexPut(int index, Data data) {
        Data old = null; //indexGet(index);
        try {
            ByteBuffer buffer = ByteBuffer.wrap(toByteArray(data));
            int nbWrite = 0;
            while (buffer.hasRemaining()) {
                nbWrite += channel.write(buffer, (long) (index * objSize));
            }
            channel.force(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return old;
    }

    @Override
    public void indexClear() {
        try {
            initZero();
        } catch (Exception e) {}
    }

    @Override
    public Iterator<Entry<Coordinate, Data>> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private byte[] toByteArray(Data data) throws Exception {
        byte[] result;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(data);
            oos.flush();
            result = bos.toByteArray();
            assert objSize == 0 || result.length == objSize;
        }
        return result;
    }

    private Data fromByteArray(byte[] bytes) throws Exception {
        //assert bytes.length == objSize;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream iis = new ObjectInputStream(bis)) {
            Data result = (Data) iis.readObject();
            return result;
        }
    }

    private void initZero() throws Exception {
        channel.position(0);
        
        ByteBuffer buffer = ByteBuffer.allocate(objSize*1024);
        byte[] buf = toByteArray(zero);
        for (int i=0;i<1024;i++) {
            buffer.put(buf);
        }
        buffer.rewind();
        int loop=size()/1024;
        for (int i=0;i<loop;i++) {
            channel.write(buffer);
        }
        
        buffer = ByteBuffer.allocate(objSize);
        buffer.put(toByteArray(zero));
        buffer.rewind();
        loop=size()%1024;
        for (int i=0;i<loop;i++) {
            channel.write(buffer);
        }
                
    }
}
