package nio;

import lib.StdOut;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class UseScatterGather {
    static private final int FIRST_HEADER_LENGTH = 2;
    static private final int SECOND_HEADER_LENGTH = 4;
    static private final int BODY_LENGTH = 6;
    static private final int MESSAGE_LENGTH = 12;

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(port);
        ssc.socket().bind(address);

        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(FIRST_HEADER_LENGTH);
        buffers[1] = ByteBuffer.allocate(SECOND_HEADER_LENGTH);
        buffers[2] = ByteBuffer.allocate(BODY_LENGTH);

        SocketChannel sc = ssc.accept();

        while (true) {

            // scatter read into buffers
            int bytesRead = 0;
            while (bytesRead < MESSAGE_LENGTH) {
                long r = sc.read(buffers);
                if (r < 0) {
                    break;
                }
                bytesRead += r;

                StdOut.println("r " + r);
                for (int i = 0; i < buffers.length; i++) {
                    ByteBuffer bb = buffers[i];
                    StdOut.println("b " + i + " " + bb.position() + " " + bb.limit());
                }
            }

            if (bytesRead < MESSAGE_LENGTH) {
                continue;
            }

            // flip buffers before processing
            for (int i = 0; i < buffers.length; i++) {
                ByteBuffer bb = buffers[i];
                bb.flip();
                StdOut.println("b " + i + " " + bb.position() + " " + bb.limit());
            }

            // gather buffers to write back.
            long bytesWritten = 0;
            while (bytesWritten < MESSAGE_LENGTH) {
                long r = sc.write(buffers);
                bytesWritten += r;
            }

            // reset buffers and make it ready to have data read into it
            for (int i = 0; i < buffers.length; i++) {
                ByteBuffer bb = buffers[i];
                bb.clear();
                StdOut.println("b " + i + " " + bb.position() + " " + bb.limit());
            }

            StdOut.println(bytesRead + " " + bytesWritten + " " + MESSAGE_LENGTH);
        }
    }
}
