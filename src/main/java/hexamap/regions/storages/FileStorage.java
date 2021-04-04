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
import java.io.ByteArrayOutputStream;
import java.io.File;
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
public class FileStorage<Data> extends AbstractStorage<Data> {

    private final FileChannel channel;
    private final Indexator indexator;
    private final int objSize;
    private int size;

    public FileStorage(Region region, Indexator indexator, Class<Data> dataClass) throws Exception {
        super(region);
        assert indexator.getRegion().equals(region);

        File f = Files.createTempFile("test-", ".data").toFile();
        this.channel = new RandomAccessFile(f, "rw").getChannel();

        this.indexator = indexator;
        this.size = 0;

        objSize = toByteArray(dataClass.getConstructor().newInstance()).length;
    }

    @Override
    protected Data safeGet(Coordinate coordinate) {
        try {
            int index = indexator.index(coordinate);
            assert index >= 0 && index < region.size();

            ByteBuffer bb = ByteBuffer.allocate(objSize);
/*
            while (channel.read(bb, (long) index) > 0) {
                out.write(buff.array(), 0, buff.position());
                buff.clear();
            }
*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected Data safePut(Coordinate coordinate, Data data) {
        int index = indexator.index(coordinate);
        assert index >= 0 && index < region.size();
        Data tmp = null;
        return tmp;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public Iterator<Entry<Coordinate, Data>> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private byte[] toByteArray(Data data) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(data);
        oos.flush();
        return bos.toByteArray();
    }
}
