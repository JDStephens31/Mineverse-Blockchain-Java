package com.jstephens.mineverse.command;


import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


//import static com.jstephens.mineverse.http.OkHttp3.sendPost;


public class MineCommand {

    public MineCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("create").then(Commands.literal("wallet").executes((command) -> {
            try {
                return setHome(command.getSource());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })));
        dispatcher.register(Commands.literal("send").then(Commands.literal("items").executes((command) -> {
            try {
                return sendItems(command.getSource());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })));
        dispatcher.register(Commands.literal("get").then(Commands.literal("items").executes((command) -> {
            try {
                return recItems(command.getSource());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })));

    }
    private int recItems(CommandSourceStack source) throws  Exception {
        ServerPlayer player = source.getPlayerOrException();
        String uuid = player.getStringUUID();
        source.sendSuccess(new TextComponent("Getting Items..."), true);
        URL url = new URL("http://192.168.1.173:8080/recIn");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);
        String jsonInputString = "{\"uuid\":\"" + uuid + "\"}";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String resString = response.toString();

            //Turns Response into an Array
            String[] items = null;
            int[] itemQuantity = null;
            items = resString.split(",");
            // Gets quantity of the items
            for (String item : items) {
                String itemNum = String.valueOf(item.charAt(1));
                String itemNumT = String.valueOf(item.charAt(2));
                String iO = itemNum.replaceAll("\\s+", "");
                String iT = itemNumT.replaceAll("\\s+", "");
                String ssItemNum = iO + iT;
                int iItemNum = Integer.parseInt(ssItemNum);
                String itemS = item;
                String itemNoBracket = itemS.replace("[", "");
                String itemNoBracketT = itemNoBracket.replace("]", "");
                String itemNoSpacesOrDigits = itemNoBracketT.replaceAll("^[\\s\\.\\d]+", "");
                String itemCaps = itemNoSpacesOrDigits.toUpperCase();
//                switchAddItem(itemCaps, iItemNum, player);
            }
            //Get items names


        }
        return 1;
    }
    private int sendItems(CommandSourceStack source) throws  Exception {
        ServerPlayer player = source.getPlayerOrException();
        NonNullList<ItemStack> inventory = player.getInventory().items;
        NonNullList<ItemStack> inventoryT = player.getInventory().items;

        String uuid = player.getStringUUID();
        source.sendSuccess(new TextComponent("Sending Items..."), true);
        URL url = new URL ("http://192.168.1.173:8080/sendIn");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);
        String jsonInputString = "{\"inventory\":\""+inventory+"\",\"uuid\":\""+ uuid +"\"}";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
        player.getInventory().clearContent();
        inventoryT = inventory;
        return 1;
    }
    private int setHome(CommandSourceStack source) throws Exception {
        ServerPlayer player = source.getPlayerOrException();
        String name = player.getStringUUID();
        source.sendSuccess(new TextComponent("Player is " + name), true);
        URL url = new URL ("http://192.168.1.173:8080/createWallet");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);
        String jsonInputString = "{\"uuid\": \""+name+"\"}";
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }

//        sendPost(name);
        return 1;
    }
    private int switchAddItem(String item, int quantity, Player player){
        switch (item){
            //DIAMOND ITEMS
            case "DIAMOND":
                player.getInventory().add(new ItemStack(Items.DIAMOND, quantity));
                break;
            case "DIAMOND_BLOCK":
                player.getInventory().add(new ItemStack(Items.DIAMOND_BLOCK, quantity));
                break;
            case "DIAMOND_ORE":
                player.getInventory().add(new ItemStack(Items.DIAMOND_ORE, quantity));
                break;
            case "DIAMOND_AXE":
                player.getInventory().add(new ItemStack(Items.DIAMOND_AXE, quantity));
                break;
            case "DIAMOND_BOOTS":
                player.getInventory().add(new ItemStack(Items.DIAMOND_BOOTS, quantity));
                break;
            case "DIAMOND_CHESTPLATE":
                player.getInventory().add(new ItemStack(Items.DIAMOND_CHESTPLATE, quantity));
                break;
            case "DIAMOND_HELMET":
                player.getInventory().add(new ItemStack(Items.DIAMOND_HELMET, quantity));
                break;
            case "DIAMOND_LEGGINGS":
                player.getInventory().add(new ItemStack(Items.DIAMOND_LEGGINGS, quantity));
                break;
            case "DIAMOND_PICKAXE":
                player.getInventory().add(new ItemStack(Items.DIAMOND_PICKAXE, quantity));
                break;
            case "DIAMOND_SHOVEL":
                player.getInventory().add(new ItemStack(Items.DIAMOND_SHOVEL, quantity));
                break;
            case "DIAMOND_SWORD":
                player.getInventory().add(new ItemStack(Items.DIAMOND_SWORD, quantity));
                break;
                //NETHERITE ITEMS
            case "NETHERITE_AXE":
                player.getInventory().add(new ItemStack(Items.NETHERITE_AXE, quantity));
                break;
            case "NETHERITE_BLOCK":
                player.getInventory().add(new ItemStack(Items.NETHERITE_BLOCK, quantity));
                break;
            case "NETHERITE_BOOTS":
                player.getInventory().add(new ItemStack(Items.NETHERITE_BOOTS, quantity));
                break;
            case "NETHERITE_CHESTPLATE":
                player.getInventory().add(new ItemStack(Items.NETHERITE_CHESTPLATE, quantity));
                break;
            case "NETHERITE_HELMET":
                player.getInventory().add(new ItemStack(Items.NETHERITE_HELMET, quantity));
                break;
            case "NETHERITE_INGOT":
                player.getInventory().add(new ItemStack(Items.NETHERITE_INGOT, quantity));
                break;
            case "NETHERITE_LEGGINGS":
                player.getInventory().add(new ItemStack(Items.NETHERITE_LEGGINGS, quantity));
                break;
            case "NETHERITE_PICKAXE":
                player.getInventory().add(new ItemStack(Items.NETHERITE_PICKAXE, quantity));
                break;
            case "NETHERITE_SCRAP":
                player.getInventory().add(new ItemStack(Items.NETHERITE_SCRAP, quantity));
                break;
            case "NETHERITE_SHOVEL":
                player.getInventory().add(new ItemStack(Items.NETHERITE_SHOVEL, quantity));
                break;
            case "NETHERITE_SWORD":
                player.getInventory().add(new ItemStack(Items.NETHERITE_SWORD, quantity));
                break;
            default:
                System.out.println(item);
                break;

        }
        return 1;
    }
}