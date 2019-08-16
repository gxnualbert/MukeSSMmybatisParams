package com.mooc.mybatis.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mooc.mybatis.bean.User;
import org.apache.ibatis.annotations.Param;

import com.mooc.mybatis.bean.Person;

public interface PersonMapper {

    /** 第一:Mybatis入参处理 单参数Mybatis会直接取出参数值给Mapper文件赋值,如#{id}**/
     public  void  deletePerson(Integer id);

    /**3-1 多参数处理--如果传递过来的参数有多个，mybatis会自动的将传递过来的参数映射成param1，param2*/
    public Person getPersonByNameAndGender(String username,String gender);


    /**3-3:如果多个参数是我们业务逻辑的数据模型，我们就可以直接传入pojo
     * 在xml中取出传递过去的值的方式为：#{属性名} 取出传入的pojo的属性值
     * **/
    public Person getPersonByNameAndGenderPojo(Person person);

    /**
     * 3-5:如果参数个数比较少，而且没有对应的JavaBean，可以封装成MPA
     * 在xml中，取值形式为：#{key} 取出Map中对应的值
     * */
    public Person getPersonByNameAndGenderMap(Map<String,Object> param);


    /**
     *  3-7 多参数处理之--@Param
     *  由于以上两种方式，需要手动创建Map（如手动创建map，然后再将user，gender放入Map中）及对象(如手动创建Person对象)，不简洁，可以使用@Param注解
     *  它可以明确指定封装参数时map的key
     * */
    public Person getPersonByNameAndGenderParam(@Param("paramKey1") String username, @Param("paramKey2")String gender);

    /**
     * 5-1 通过foreach 循环获取数据
     * */
    public List<Person> getPersonsByIds(int[] ids);


//    public Person getPersonByCollection(Collection list);

    public Person getPersonByCollection(@Param("test") int[] ids);



//    public int addPersons(@Param("persons") List<Person> persons);
    public int addPersons( List<Person> persons);

    /*public int addPerson(Person person);*/

    public int addPerson(User user);

    public List<Person> getAllPersons();








}
