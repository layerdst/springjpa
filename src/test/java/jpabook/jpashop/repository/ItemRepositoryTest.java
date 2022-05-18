package jpabook.jpashop.repository;

import jpabook.jpashop.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

//    @Test
//    public void save(){
//        Item item = new Item();
//        itemRepository.save(item);
//    }

    @Test
    public void save(){
        Item item = new Item("a");
        itemRepository.save(item);
    }
}