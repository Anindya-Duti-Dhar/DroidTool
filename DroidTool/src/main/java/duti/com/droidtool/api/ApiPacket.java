package duti.com.droidtool.api;

import java.util.List;

/**
 * Created by imrose on 5/11/2018.
 */

public class ApiPacket<T> {
    private T Packet;
    private List<T> PacketList;

    public T getPacket() {
        return Packet;
    }

    public void setPacket(T packet) {
        Packet = packet;
    }

    public List<T> getPacketList() {
        return PacketList;
    }

    public void setPacketList(List<T> packetList) {
        PacketList = packetList;
    }
}
