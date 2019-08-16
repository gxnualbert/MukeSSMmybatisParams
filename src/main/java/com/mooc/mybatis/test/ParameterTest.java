package com.mooc.mybatis.test;

import com.mooc.mybatis.bean.Person;
import com.mooc.mybatis.dao.PersonMapper;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by imooc
 */
public class ParameterTest {

    public static SqlSessionFactory sqlSessionFactory = null;

    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            String resource = "mybatis-config.xml";
            try {
                Reader reader = Resources.getResourceAsReader(resource);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sqlSessionFactory;
    }


    /**
     * 第一:Mybatis入参处理 单参数Mybatis会直接取出参数值给Mapper文件赋值,如#{id}
     **/
    public void deletePerson() {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        //通过sqlSession的getMapper()方法可以获取PersonMapper的动态代理类，返回值是一个PersonMapper类型
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        personMapper.deletePerson(2);
        //对于mybatis来说，它的事务是不会自动提交的，所以，当我们真正的去执行delete操作之后。
        // 我们需要借助sqlSession来做事务的提交
        sqlSession.commit();
    }

    /**
     * 3-1 多参数处理--Map。 如果参数个数比较少，而且没有对应的javaBean, 可以封装成Map
     */
    public void testPersonByNameAndGender() {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonByNameAndGender("wangwu", "f");
        System.out.println(person);
    }


    /**
     * 3-3:如果多个参数是我们业务逻辑的数据模型，我们就可以直接传入pojo
     *      * 在xml中取出传递过去的值的方式为：#{属性名} 取出传入的pojo的属性值
     * */
    public void testPersonByNameAndGenderPojo() {

        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonByNameAndGenderPojo(new Person("wangwu", "f"));
        System.out.println(person);

    }

    /**
     * 3-5 如果参数个数比较少，而且没有对应的JavaBean，可以封装成Map
     *     在xml中，取值形式为：#{key} 取出Map中对应的值
     * */
    public void testPersonByNameAndGenderMap() {

        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Map<String,Object> param=new HashMap<String, Object>();
        param.put("name","wangwu");
        param.put("gender","f");

        Person person = personMapper.getPersonByNameAndGenderMap(param);
        System.out.println(person);
    }

    /**
     * 3-7 多参数处理之--@Param
     *     由于以上两种方式，需要手动创建Map（如手动创建map，然后再将user，gender放入Map中）及对象(如手动创建Person对象)，不简洁，可以使用@Param注解
     *     它可以明确指定封装参数时map的key
     * */
    public void testPersonByNameAndGenderParams() {

        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonByNameAndGenderParam("wangwu","f");
        System.out.println(person);
    }

    /**
     * 5-1
     * 通过foreach 循环获取数据
     * */
    public void testForeach() {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        List<Person> personList = personMapper.getPersonsByIds(new int[]{1, 2, 3, 4, 5});
        System.out.println(personList);
    }

    /**
     *  6-1
     *  使用mybatis 进行批量插入
     * **/
    public void processMybatisBatch() {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);

        List<Person> persons = new ArrayList<Person>();

        for (int i = 0; i < 5; i++) {
            Person person = new Person("autotest" + i, "autotest" + i + "@gmail.com", "f");
            persons.add(person);
        }
        personMapper.addPersons(persons);
        sqlSession.commit();
    }

    /** 6-2
     * 这种方式是借助MySQL的特性的，连接URL中有allowMultiQueries=true
     * 使用方式mybatis 方式二进行批量插入
     * */
    public void processMybatisBatchWay2() {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);

        List<Person> persons = new ArrayList<Person>();

        for (int i = 0; i < 5; i++) {
            Person person = new Person("addPersonsWay2" + i, "addPersonsWay2" + i + "@gmail.com", "m");
            persons.add(person);
        }
        personMapper.addPersonsWay2(persons);
        sqlSession.commit();
    }



    public void testCollection() {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession();

        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);

        Person person = personMapper.getPersonByCollection(new int[]{1, 2, 3, 4, 5});

        System.out.println(person);

    }





    public void testBatchForExecutor() {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession(ExecutorType.BATCH);

        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);

//        for (int i = 0; i < 10000; i++) {
//            personMapper.addPerson(new Person("tom", "tom@imooc.com", "F"));
//        }
        sqlSession.commit();
        sqlSession.close();
    }


    public static void main(String[] args) {
        /**
         * 第一:Mybatis入参处理 单参数Mybatis会直接取出参数值给Mapper文件赋值,如#{id}*
         * */
//        new ParameterTest().deletePerson();


        /**
         * 3-1  多参数处理-如果传递过来的参数有多个，mybatis会自动的将传递过来的参数映射成param1，param2
         * */
//        new ParameterTest().testPersonByNameAndGender();


        /**
         * 3-3  如果多个参数是我们业务逻辑的数据模型，我们就可以直接传入pojo
         *      在xml中取出传递过去的值的方式为：#{属性名} 取出传入的pojo的属性值
         * */
//        new ParameterTest().testPersonByNameAndGenderPojo();

        /**
         * 3-5  如果参数个数比较少，而且没有对应的JavaBean，可以封装成Map
         *      在xml中，取值形式为：#{key} 取出Map中对应的值
         * */
//        new ParameterTest().testPersonByNameAndGenderMap();


        /**
         * 3-7  多参数处理之--@Param
         *      *  由于以上两种方式，需要手动创建Map（如手动创建map，然后再将user，gender放入Map中）及对象(如手动创建Person对象)，不简洁，可以使用@Param注解
         *      *  它可以明确指定封装参数时map的key
         * */
//        new ParameterTest().testPersonByNameAndGenderParams();

        /**
         * 5-1: 通过foreach 循环获取数据
         * */
//        new ParameterTest().testForeach();

        /**
         * 6-1
         * 使用mybatis 进行批量插入
         *
         * */
//        new ParameterTest().processMybatisBatch();

        /**
         * 6-2
         * 这种方式是借助MySQL的特性的，连接URL中有allowMultiQueries=true
         * 使用mybatis 方式二进行批量插入
         *
         * */
        new ParameterTest().processMybatisBatchWay2();





    }


}
