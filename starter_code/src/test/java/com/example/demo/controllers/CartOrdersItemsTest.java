package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.persistence.criteria.Order;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CartOrdersItemsTest {

    UserRepository userRepository=mock(UserRepository.class);

    CartRepository cartRepository=mock(CartRepository.class);

    ItemRepository itemRepository=mock(ItemRepository.class);

    OrderRepository orderRepository =mock(OrderRepository.class);

    CartController cartController;

    ItemController itemController;

    OrderController orderController;



    @Before
    public void setup(){

        cartController =new CartController();
        Cart cart = new Cart();
        testUtilities.injectObject(cartController,"userRepository",userRepository);
        testUtilities.injectObject(cartController,"cartRepository",cartRepository);
        testUtilities.injectObject(cartController,"itemRepository",itemRepository);

        itemController = new ItemController();
        testUtilities.injectObject(itemController,"itemRepository",itemRepository);

        orderController = new OrderController();
        testUtilities.injectObject(orderController,"userRepository",userRepository);
        testUtilities.injectObject(orderController,"orderRepository",orderRepository);

        User user = new User();

        user.setId(0);
        user.setUsername("username");
        user.setPassword("userPassword");
        user.setCart(cart);

        Item item = new Item();

        item.setId(1L);
        item.setName("phone");
        item.setPrice(BigDecimal.valueOf(900.99));
        item.setDescription("smart phone");

        when(userRepository.findByUsername("username")).thenReturn(user);
        when(itemRepository.findById(1l)).thenReturn(Optional.of(item));

        cart.addItem(item);

    }

    @Test
    public void add_cart(){

        ModifyCartRequest req = new  ModifyCartRequest();

        req.setUsername("username");
        req.setItemId(1L);
        req.setQuantity(1);

        ResponseEntity<Cart> response= cartController.addTocart(req);

        Assert.assertEquals(200,response.getStatusCodeValue());

        Cart cart = response.getBody();

        Assert.assertNotNull(cart);
    }

    @Test
    public void remove_cart(){

        ModifyCartRequest req = new  ModifyCartRequest();

        req.setUsername("username");
        req.setItemId(1L);
        req.setQuantity(1);

        ResponseEntity<Cart> response= cartController.addTocart(req);

        Assert.assertEquals(200,response.getStatusCodeValue());

        Assert.assertTrue(cartController.removeFromcart(req).hasBody());
    }

    @Test
    public void getAll_Itmes(){
        Assert.assertNotNull(itemController.getItems());
    }

    @Test
    public void get_item_by_id(){
        Assert.assertNotNull(itemController.getItemById(1L));
    }

    @Test
    public void get_item_by_name(){
        Assert.assertNotNull(itemController.getItemsByName("phone"));
    }


    @Test
    public void submit_order(){
       ResponseEntity<UserOrder> response= orderController.submit("username");

       Assert.assertNotNull(response.getBody());
       Assert.assertEquals(200,response.getStatusCodeValue());
    }

    @Test
    public  void get_user_order(){
      ResponseEntity<List<UserOrder>> response=  orderController.getOrdersForUser("username");

      Assert.assertEquals(200,response.getStatusCodeValue());

    }

}
