package com.ztory.lib.sleek;

//import junit.framework.TestCase;

import com.ztory.lib.sleek.val.Val;
import com.ztory.lib.sleek.val.ValAction;
import java.util.concurrent.CountDownLatch;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SleekTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {

  }

  @Test
  public void testEqualsAndHashCode() throws Exception {
    final String nameString = "Jonny";
    Assert.assertTrue(Val.with(nameString).equals(Val.with("Jonny")));
    //Assert.assertTrue(Val.with(nameString).equals("Jonny"));
    Assert.assertEquals(nameString, "Jonny");
    Assert.assertEquals(Val.with(nameString), Val.with("Jonny"));
    Assert.assertEquals(Val.with(nameString).hashCode(), Val.with("Jonny").hashCode());
  }

  @Test
  public void testValWith() throws Exception {
    final String titleString = "Act 1";
    Assert.assertTrue(Val.with(titleString).isSet());
    Assert.assertFalse(Val.with(titleString).isNull());
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    Val.with(titleString).ifSet(new ValAction<String>() {
      @Override
      public void action(String value) {
        countDownLatch.countDown();
      }
    });
    Assert.assertEquals(countDownLatch.getCount(), 0);
  }

  @Test
  public void testValEmptyString() throws Exception {
    final String stringValue = "";
    Val<String> val = new Val<>(stringValue);
    Assert.assertTrue(stringValue.equals(val.get()));
    Assert.assertFalse(val.isNull());
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    val.ifNull(new Runnable() { @Override public void run() {
      countDownLatch.countDown();
    }});
    Assert.assertEquals(countDownLatch.getCount(), 1);// still == 1 because ifNull() is not called
    val.ifSet(new ValAction<String>() { @Override public void action(String value) {
      countDownLatch.countDown();
    }});
    Assert.assertEquals(countDownLatch.getCount(), 0);
  }

}
