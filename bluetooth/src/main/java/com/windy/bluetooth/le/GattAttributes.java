package com.windy.bluetooth.le;

import java.util.UUID;

/**
 * author: wang
 * time: 2015/10/13
 * description:
 */
public class GattAttributes {
    public static final UUID notifyUUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final UUID char1UUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static final UUID char6UUID = UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb");
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");

    public static final String BROAD_INFO = "bluetooth_information";

    //--------指令
    public static final byte[] open = {0x11};
    public static final byte[] close = {0x10};
    public static final byte[] read = {0x21};

}
