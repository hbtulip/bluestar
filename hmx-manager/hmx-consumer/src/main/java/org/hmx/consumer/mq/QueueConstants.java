package org.hmx.consumer.mq;

public interface QueueConstants {
    // 消息交换
    String MESSAGE_EXCHANGE = "message.direct.myexchange";
    // 消息队列名称
    String MESSAGE_QUEUE_NAME = "message.myqueue";
    // 消息路由键
    String MESSAGE_ROUTE_KEY = "message.myroute";

    // 死信消息交换
    String MESSAGE_EXCHANGE_DL = "message.direct.dlexchange";
    // 死信消息队列名称
    String MESSAGE_QUEUE_NAME_DL = "message.dlqueue";
    // 死信消息路由键
    String MESSAGE_ROUTE_KEY_DL = "message.dlroute";
}
