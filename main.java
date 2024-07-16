import com.cqrs.ServiceProducerApplication;
import com.example.serviceconsumer.MessageTest;

public class main {
    public static void main(String[] args){
        SpringApplication.run(ServiceConsumerApplication.class, args);
        SpringApplication.run(ServiceProducerApplication.class, args);
        SpringApplication.run(MessageTest.java, args);
    }
}
