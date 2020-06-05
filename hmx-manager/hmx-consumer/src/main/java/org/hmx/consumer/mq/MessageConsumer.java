package org.hmx.consumer.mq;

import org.hmx.utils.api.vo.WechatCardVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;


/*
#设置消费端手动 ack
spring.rabbitmq.listener.simple.acknowledge-mode=manual
#消费者最小数量
spring.rabbitmq.listener.simple.concurrency=1
#消费之最大数量
spring.rabbitmq.listener.simple.max-concurrency=10
#在单个请求中处理的消息个数，他应该大于等于事务数量(unack的最大数量)
spring.rabbitmq.listener.simple.prefetch=2
*/

/*
@Component
public class MessageConsumer {
    @RabbitListener(queues = QueueConstants.MESSAGE_QUEUE_NAME)
    public void processMessage(Channel channel,Message  message) {
        System.out.println("MessageConsumer收到消息："+new String(message.getBody()));
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            
            //手工ack
            //long deliveryTag = message.getMessageProperties().getDeliveryTag();
        	//channel.basicAck(deliveryTag,true);
        	
        } catch (Exception e) {
            
        } 
    }
}
*/

@Component
@RabbitListener(queues = QueueConstants.MESSAGE_QUEUE_NAME)
public class MessageConsumer {
    
	@RabbitHandler
    public void onProcessMessage1(String msg) {
        System.out.println("processMessage1收到消息："+ msg);
    }
  
    @RabbitHandler
    public void onProcessMessage2(WechatCardVo msg) {
        System.out.println("processMessage2收到消息："+ msg.getUrl());
    }
    
}


