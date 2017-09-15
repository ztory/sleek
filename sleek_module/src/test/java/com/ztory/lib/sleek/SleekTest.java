package com.ztory.lib.sleek;

//import junit.framework.TestCase;

import com.ztory.lib.sleek.assumption.Assumeable;
import com.ztory.lib.sleek.assumption.Assumption;
import com.ztory.lib.sleek.assumption.Func;
import com.ztory.lib.sleek.assumption.SimpleAssumption;
import com.ztory.lib.sleek.mapd.Mapd;
import com.ztory.lib.sleek.mapd.MapdRule;
import com.ztory.lib.sleek.util.UtilDownload;
import com.ztory.lib.sleek.util.UtilExecutor;
import com.ztory.lib.sleek.val.Val;
import com.ztory.lib.sleek.val.ValAction;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
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
  public void testMapdValidation() throws Exception {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "Jonny");
    map.put("count", 1);
    map.put("pro", false);
    Map<String, MapdRule> validationRules = Mapd.createValidationRules(
        Mapd.keyValue("name", MapdRule.required(String.class)),
        Mapd.keyValue("count", MapdRule.required(Number.class)),
        Mapd.keyValue("pro", MapdRule.optional(Boolean.class))
    );
    Mapd.validateMap(validationRules, map);
  }

  @Test
  public void testMapdGet() throws Exception {
    Map<String, Object> map = new HashMap<>();
    Object object = 1;
    map.put("obj", object);
    map.put("str", "Jonny");
    Assert.assertNotNull(Mapd.get(map, "obj", Number.class));
    Assert.assertNotNull(Mapd.get(map, "obj", Integer.class));
    Assert.assertNull(Mapd.get(map, "obj", Double.class));
    Assert.assertNotNull(Mapd.get(map, "str", String.class));
    Assert.assertNull(Mapd.get(map, "str", Number.class));
  }

  @Test
  public void testInputStreamToString() throws Exception {
    String textString = "Hello SleekTest World!";
    byte[] textStringBytes = textString.getBytes("UTF-8");
    String textString2 = UtilDownload.inputStreamToString(
        new ByteArrayInputStream(textStringBytes)
    );
    Assert.assertEquals(textString, textString2);
  }

  @Test
  public void testAssumptionParam() throws Exception {
    final String stringAppend = "Append";
    Func<String, String> stringAppender = new Func<String, String>() {
      @Override
      public String invoke(String param) throws Exception {
        if (param != null) {
          return param + stringAppend;
        } else {
          return stringAppend;
        }
      }
    };
    Assumption<String> assumptionOne = new SimpleAssumption<>(
        UtilExecutor.CPU_QUAD,
        null,
        stringAppender
    );
    Assumption<String> assumptionTwo = new SimpleAssumption<>(
        UtilExecutor.CPU_QUAD,
        assumptionOne,
        stringAppender
    );
    Assumption<String> assumptionThree = new SimpleAssumption<>(
        UtilExecutor.CPU_QUAD,
        assumptionTwo,
        stringAppender
    );
    Assert.assertEquals(stringAppend, assumptionOne.get());
    Assert.assertEquals(false, assumptionOne.isCancelled());
    Assert.assertEquals(true, assumptionOne.isDone());
    Assert.assertEquals(true, assumptionOne.isCorrect());
    Assert.assertEquals(null, assumptionOne.getException());
    Assert.assertEquals(true, assumptionOne.isSet());
    Assert.assertEquals(false, assumptionOne.isNull());

    Assert.assertEquals(stringAppend + stringAppend, assumptionTwo.get());
    Assert.assertEquals(false, assumptionTwo.isCancelled());
    Assert.assertEquals(true, assumptionTwo.isDone());
    Assert.assertEquals(true, assumptionTwo.isCorrect());
    Assert.assertEquals(null, assumptionTwo.getException());
    Assert.assertEquals(true, assumptionTwo.isSet());
    Assert.assertEquals(false, assumptionTwo.isNull());

    Assert.assertEquals(stringAppend + stringAppend + stringAppend, assumptionThree.get());
    Assert.assertEquals(false, assumptionThree.isCancelled());
    Assert.assertEquals(true, assumptionThree.isDone());
    Assert.assertEquals(true, assumptionThree.isCorrect());
    Assert.assertEquals(null, assumptionThree.getException());
    Assert.assertEquals(true, assumptionThree.isSet());
    Assert.assertEquals(false, assumptionThree.isNull());
  }

  @Test
  public void testAssumption() throws Exception {

    final CountDownLatch countDownLatch = new CountDownLatch(3);

    Assumption<Integer> chainAssumption = new SimpleAssumption<>(
        UtilExecutor.CPU_QUAD,
        null,
        new Func<Void, Integer>() {
          @Override
          public Integer invoke(Void param) throws Exception {
            countDownLatch.countDown();
            return 44;
          }
        }
    );

    Assumption<String> assumption = new SimpleAssumption<>(
        UtilExecutor.CPU_QUAD,
        null,
        new Func<Object, String>() {
          @Override
          public String invoke(Object param) throws Exception {
            return "dahString";
          }
        }
    ).correct(new Assumeable<String>() {
      @Override
      public void assume(String result) {
        countDownLatch.countDown();
      }
    }).wrong(new Assumeable<Exception>() {
      @Override
      public void assume(Exception result) {

      }
    }).done(new Assumeable<Assumption<String>>() {
      @Override
      public void assume(Assumption<String> result) {
        countDownLatch.countDown();
      }
    });

    countDownLatch.await();

    Assert.assertEquals("dahString", assumption.get());
    Assert.assertEquals(false, assumption.isCancelled());
    Assert.assertEquals(true, assumption.isDone());
    Assert.assertEquals(true, assumption.isCorrect());
    Assert.assertEquals(null, assumption.getException());
    Assert.assertEquals(true, assumption.isSet());
    Assert.assertEquals(false, assumption.isNull());
    Assert.assertEquals(Integer.valueOf(44), chainAssumption.get());
    Assert.assertEquals(false, chainAssumption.isCancelled());
    Assert.assertEquals(true, chainAssumption.isDone());
    Assert.assertEquals(true, chainAssumption.isCorrect());
    Assert.assertEquals(null, chainAssumption.getException());
    Assert.assertEquals(true, chainAssumption.isSet());
    Assert.assertEquals(false, chainAssumption.isNull());

    Assumption<String> wrongAssumption = new SimpleAssumption<>(
        null,
        null,
        new Func<Object, String>() {
          @Override
          public String invoke(Object param) throws Exception {
            throw new RuntimeException("Test exception!");
          }
        }
    );

    CountDownLatch wrongCountDownLatch = new CountDownLatch(1);

    try {
      wrongAssumption.get();
    } catch (ExecutionException e) {
      wrongCountDownLatch.countDown();
    }

    wrongCountDownLatch.await();

    Assert.assertEquals(false, wrongAssumption.isCancelled());
    Assert.assertEquals(true, wrongAssumption.isDone());
    Assert.assertEquals(false, wrongAssumption.isCorrect());
    Assert.assertNotNull(wrongAssumption.getException());
    Assert.assertEquals(false, wrongAssumption.isSet());
    Assert.assertEquals(true, wrongAssumption.isNull());

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
