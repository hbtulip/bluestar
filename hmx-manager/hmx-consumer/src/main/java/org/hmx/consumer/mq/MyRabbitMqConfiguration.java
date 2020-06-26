package org.hmx.consumer.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRabbitMqConfiguration {

	/**
	 * 交换配置 Direct类型的Exchange
	 *
	 * @return
	 */
	@Bean
	public DirectExchange messageDirectExchange() {
		return (DirectExchange) ExchangeBuilder.directExchange(QueueConstants.MESSAGE_EXCHANGE).durable(true).build();
	}

	/**
	 * 消息队列声明
	 *
	 * @return
	 */
	@Bean
	public Queue messageQueue() {
		return QueueBuilder.durable(QueueConstants.MESSAGE_QUEUE_NAME).build();
	}

	/**
	 * 消息绑定
	 *
	 * @return
	 */
	@Bean
	public Binding messageBinding() {
		return BindingBuilder.bind(messageQueue()).to(messageDirectExchange()).with(QueueConstants.MESSAGE_ROUTE_KEY);
	}
	
	
	/*
	 * Topic类型的Exchange
	//队列
    public static final String QUEUE_NEWS = "queue_news";
    public static final String QUEUE_WEATHER = "queue_weather";
    public static final String QUEUE_NEWS_WEATHER = "queue_news_weather";
    //交换机
    public static final String EXCHANGE_TOPIC_KAIFENG = "exchange_topic_kaifeng";
    //交换机与队列绑定的RoutingKey
    public static final String ROUTINGKEY_NEWS = "*.news";
    public static final String ROUTINGKEY_WEATHER = "*.weather";
    public static final String ROUTINGKEY_ALL = "kaifeng.*";

    //声明队列
    @Bean(QUEUE_NEWS)
    public Queue QUEUE_NEWS(){    //新闻的队列
        return new Queue(QUEUE_NEWS);
    }
    @Bean(QUEUE_WEATHER)
    public Queue QUEUE_WEATHER(){  //天气的队列
        return new Queue(QUEUE_WEATHER);
    }
    @Bean(QUEUE_NEWS_WEATHER)
    public Queue QUEUE_NEWS_WEATHER(){  //新闻和天气的队列
        return new Queue(QUEUE_NEWS_WEATHER);
    }

    //声明交换机
    @Bean(EXCHANGE_TOPIC_KAIFENG)
    public Exchange EXCHANGE_TOPIC_INFORM(){
        //声明了一个Topic类型的交换机，durable是持久化（重启rabbitmq这个交换机不会被自动删除）
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC_KAIFENG).durable(true).build();
    }

    //声明news队列和交换机绑定关系，并且指定RoutingKey
    @Bean
    public Binding NEWS_BINDING_TOPIC(@Qualifier(QUEUE_NEWS) Queue queue,
                                      @Qualifier(EXCHANGE_TOPIC_KAIFENG) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_NEWS).noargs();
    }
    //声明weather队列和交换机绑定关系，并且指定RoutingKey
    @Bean
    public Binding WEATHER_BINDING_TOPIC(@Qualifier(QUEUE_WEATHER) Queue queue,
                                      @Qualifier(EXCHANGE_TOPIC_KAIFENG) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_WEATHER).noargs();
    }
    //声明news+weather队列和交换机绑定关系，并且指定RoutingKey
    @Bean
    public Binding NEWS_WEATHER_BINDING_TOPIC(@Qualifier(QUEUE_NEWS_WEATHER) Queue queue,
                                       @Qualifier(EXCHANGE_TOPIC_KAIFENG) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_ALL).noargs();
    }
    */

}