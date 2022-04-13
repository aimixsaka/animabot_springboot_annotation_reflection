package com.yanfang.animabot.handlers;

import com.yanfang.animabot.config.LongConfig;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class OtherEventHandler extends SimpleListenerHost
{
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception)
    {
        log.warn(exception.getMessage());
    }

    // 新成员入群事件处理
    @EventHandler
    public ListeningStatus onMessage(@NotNull MemberJoinEvent event) throws Exception
    {

        Long targetID = event.getMember().getId();
        MessageChain messages = new MessageChainBuilder()
                .append(new At(targetID))
                .append(LongConfig.GROUP_WELCOME_MESSAGE)
                .build();
        event.getGroup().sendMessage(messages);
        return ListeningStatus.LISTENING;

    }

    // 群成员离开事件处理
    @EventHandler
    public ListeningStatus onMessage(@NotNull MemberLeaveEvent event) throws Exception
    {
        String leaverName = event.getMember().getNick();
        event.getGroup().sendMessage(leaverName + "離れないでください");
        return ListeningStatus.LISTENING;
    }

    // 加好友处理
    @EventHandler
    public ListeningStatus onMessage(@NotNull NewFriendRequestEvent event) throws Exception
    {
        if (LongConfig.mastersIDSet.contains(event.getFromId()))
        {
            event.accept();
            return ListeningStatus.LISTENING;
        }
        else
        {
            event.reject(false);
            return ListeningStatus.LISTENING;
        }
    }
}
