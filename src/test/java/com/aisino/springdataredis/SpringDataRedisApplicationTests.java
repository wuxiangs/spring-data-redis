package com.aisino.springdataredis;

import com.aisino.springdataredis.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Test
    public void testString(){
        //存储数据
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("name","吴祥");

        //获取数据
        String name = (String) ops.get("name");
        System.out.println(name);

        //层级目录存储数据
        ops.set("user:01","李四");

        //添加多条数据
        Map<String,String> map=new HashMap<>();
        map.put("age","20");
        map.put("address","上海");
        ops.multiSet(map);

        //获取多条数据
        List<String> list=new ArrayList<>();
        list.add("name");
        list.add("age");
        list.add("address");
        List list1 = ops.multiGet(list);
        list1.forEach(System.out::println);

        //删除数据
        redisTemplate.delete("name");
    }

    @Test
    public void testHash(){
        HashOperations ops = redisTemplate.opsForHash();

        /**
         * 添加数据
         * 第一个参数 redis的key
         * 第二个参数 hash的key
         * 第三个参数 hash的value
         */
        ops.put("user","name","张三");

        //获取数据
        String name = (String) ops.get("user", "name");
        System.out.println(name);

        //添加多条数据
        Map<String,String> map=new HashMap<>();
        map.put("age","20");
        map.put("address","上海");
        ops.putAll("user",map);

        //获取多条数据
        List<String> list=new ArrayList<>();
        list.add("name");
        list.add("age");
        list.add("address");
        List user = ops.multiGet("user", list);
        user.forEach(System.out::println);

        //获取hash类型所有的数据
        Map<String,String> entries = ops.entries("user");
        entries.entrySet().forEach(e->{
            System.out.println(e.getKey()+"----"+e.getValue());
        });

        //hash的删除
        ops.delete("user","age");

    }


    //操作list
    @Test
    public void testList(){
        ListOperations opsForList = redisTemplate.opsForList();
        //左添加
        opsForList.leftPush("students","张三");
        opsForList.leftPush("students","李四");
        opsForList.leftPush("students","李四","康康");
        //右添加
        opsForList.rightPush("students","赵六");
        //获取数据
        List list = opsForList.range("students",0,4);
        list.forEach(System.out::println);
        //获取总条数
        Long count = opsForList.size("students");

        //左弹出
        opsForList.leftPop("students");
        //右弹出
        opsForList.rightPop("students");

        //删除
       // opsForList.remove("students",1,"李四");
    }

    /**
     * set操作
     */
    @Test
    public void testSet(){
        SetOperations opsForSet = redisTemplate.opsForSet();
        //添加数据
        opsForSet.add("letters","aaa","bbb","ccc","ddd","eee");
        //获取数据
        Set letters = opsForSet.members("letters");
        letters.forEach(System.out::println);
        //删除数据
        opsForSet.remove("letters","aaa","bbb");
    }

    //操作sorted set
    @Test
    public void testSortedSet(){
        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        //添加数据
        ZSetOperations.TypedTuple<Object> typedTuple1=new DefaultTypedTuple<Object>("张三",7D);
        ZSetOperations.TypedTuple<Object> typedTuple2=new DefaultTypedTuple<Object>("李四",1D);
        ZSetOperations.TypedTuple<Object> typedTuple3=new DefaultTypedTuple<Object>("王五",2D);
        ZSetOperations.TypedTuple<Object> typedTuple4=new DefaultTypedTuple<Object>("赵六",5D);
        ZSetOperations.TypedTuple<Object> typedTuple5=new DefaultTypedTuple<Object>("鬼脚七",6D);
        Set<ZSetOperations.TypedTuple<Object>> set=new HashSet<>();
        set.add(typedTuple1);
        set.add(typedTuple2);
        set.add(typedTuple3);
        set.add(typedTuple4);
        set.add(typedTuple5);
        opsForZSet.add("score",set);

        //获取数据
        Set score = opsForZSet.range("score", 0, 4);
        score.forEach(System.out::println);

        //删除数据
        opsForZSet.remove("score","张三");
    }

    //获取所有的key
    @Test
    public void testKeys(){
        Set keys = redisTemplate.keys("*");
        keys.forEach(System.out::println);
    }

    //设置失效时间
    @Test
    public void testExpire(){
        ValueOperations ops = redisTemplate.opsForValue();
        //添加key的时候 设置失效时间
        ops.set("wx","哈哈哈",10, TimeUnit.SECONDS);

        //获取失效时间
        Long second = redisTemplate.getExpire("wx");
        System.out.println(second);

        //给已经存在的key设置失效时间
        redisTemplate.expire("age",10,TimeUnit.SECONDS);


    }

}
