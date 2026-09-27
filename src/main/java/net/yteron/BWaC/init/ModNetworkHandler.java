package net.yteron.BWaC.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.yteron.BWaC.block.system.ClimbingSystem;
import net.yteron.BWaC.block.system.HandlerServer;

public class ModNetworkHandler {

    private static final String PROTOCOL = "1";
    public static SimpleChannel CHANNEL;

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("bwac", "main"),
                () -> PROTOCOL,
                PROTOCOL::equals,
                PROTOCOL::equals
        );

        int id = 0;
        CHANNEL.registerMessage(
                id++,
                HandlerServer.class,
                HandlerServer::encode,
                HandlerServer::new,
                HandlerServer::handle
        );
    }
}
