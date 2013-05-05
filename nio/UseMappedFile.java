package nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class UseMappedFile {
    static private final int START = 0;
    static private final int SIZE = 1024;

    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("/tmp/tmp.txt", "rw");
        FileChannel fc = file.getChannel();

        // map first 1024 bytes of file '/tmp/tmp.txt' into memory
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, START, SIZE);

        mbb.put(0, (byte)97);
        mbb.put(1023, (byte)122);

        file.close();
    }
}
