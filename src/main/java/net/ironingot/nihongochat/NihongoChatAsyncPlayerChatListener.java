package net.ironingot.nihongochat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.SpongeEventHandler;
import org.spongepowered.api.event.player.AsyncPlayerChatEvent;

import biscotte.kana.Kana;
import net.ironingot.translator.KanaKanjiTranslator;

public class NihongoChatAsyncPlayerChatListener {
    public NihongoChat plugin;

    private static final String avoidingString = 
            "[^\u0020-\u007E]|\u00a7|u00a74u00a75u00a73u00a74v|^http|^[A-Z#!<>]";
    private static final Pattern avoidingPattern = Pattern.compile(avoidingString);

    public NihongoChatAsyncPlayerChatListener(NihongoChat plugin) {
        this.plugin = plugin;
    }

    @SpongeEventHandler(order=Order.LAST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        if (message.startsWith("/")) {
            return;
        }

        if (plugin.getConfigHandler().getUserMode(player.getName()).equals(Boolean.FALSE)) {
            return;
        }

        Matcher matcher = avoidingPattern.matcher(message);
        if (!matcher.find(0)) {
            Kana kana = new Kana();
            kana.setLine(message);
            kana.convert();

            String kanaMessage = kana.getLine();

            if (message.equals(kanaMessage)) {
                return;
            }

            StringBuilder stringBuilder = new StringBuilder();
            String kanjiMessage = kanaMessage;

            if (plugin.getConfigHandler().getUserKanjiConversion(player.getName()).equals(Boolean.TRUE)) {
                kanjiMessage = KanaKanjiTranslator.translate(kanaMessage);
            }

            if (kanjiMessage.length() > 0) {
                stringBuilder.append(kanjiMessage);
            } else {
                stringBuilder.append(kanaMessage);
            } 

            // stringBuilder.append(ChatColor.GRAY).append(" ").append(message);
            // event.setMessage(stringBuilder.toString());
            event.getGame().broadcastMessage(stringBuilder.toString());
        }
    }
}
