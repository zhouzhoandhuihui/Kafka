import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

//生产者
public class Kafka {
    public static void main(String[] args) {
        //kafka的连接，类型为list模式
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        /**
         * ack是判别请求条件是否完善的条件（就是判断是不是成功发送了）。
         * 1，ack=0，那么生产者将不等待任何消息确认。消息将立刻添加到socket缓冲区中并考虑发送，在这种情况下不能保证消息被服务器接收到，并且重试机制不会生效（因为客户端不知道有没有故障）
         * 2，ack=1，意味着leader写入消息到本地日志就立即响应，而不等待所有follower应答。在这种情况下，如果响应消息之后但follower还未复制之前leader立即故障，那么消将会丢失
         * 3，ack=all，意味着leader将等待所有副本同步后应答消息，此配置保障消息不会丢失。等价于ack=-1
         * 我们指定了“all”将会阻塞消息，这种性能最低，但是是最可靠的
         */
        props.put("acks", "all");
        //retries，如果请求失败，生产者会自动重试，我们指定0次，如果重启重试，则会有重复消息的可能性
        props.put("retries", 0);
        //指定缓存大小
        props.put("batch.size", 16384);
        //linger.ms=0。在不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
        props.put("linger.ms", 1);
        //buffer.memory 控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，其他发送调用将被阻塞，阻塞时间的阈值通过max.block.ms设定，之后它将抛出一个TimeoutException。
        props.put("buffer.memory", 33554432);
        //key.serializer和value.serializer示例，将用户提供的key和value对象ProducerRecord转换成字节，你可以使用附带的ByteArraySerializaer或StringSerializer处理简单的string或byte类型。
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for(int i = 0; i < 10; i++){
           //send()方法是异步的
            producer.send(new ProducerRecord<String, String>("test", "tt", "测试"));
        }
        producer.close();
    }
}
