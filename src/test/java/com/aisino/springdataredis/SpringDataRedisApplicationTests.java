package com.aisino.springdataredis;

import com.aisino.springdataredis.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class SpringDataRedisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void initConnect() {
        ValueOperations<String, String> opt = redisTemplate.opsForValue();
        opt.set("kk","kangkang");
    }

    /**
     * 测试序列话
     */
    @Test
    public void testSerial(){
        User user=new User();
        user.setId(1);
        user.setName("zhangsan");
        user.setAge(18);
        redisTemplate.opsForValue().set("user",user);
        Object user1 = redisTemplate.opsForValue().get("user");
        System.out.println(user1);

    }
}
