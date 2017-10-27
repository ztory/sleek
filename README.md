# Sleek

Sleek is an Android UI framework that tries to simplify complex UI hierarchy in the Android world.

## Features
- **Style UI in CSS** For people who like CSS more than XML.
- **60 fps smoothness** Minimum overhead when drawing and animating the UI.
- **Easy to use** Creating and configuring appearance, layout and touch have never been easier.
- **Flexible** Every class and interface is built for you to extend and customize to your liking.
- **Plug & Play** Sleek functionality and Android Views can be used side by side seamlessly.
- **Fun** Create 100 UI elements that randomly animates with some weird canvas manipulation why don't ya!

## The Sleek interface
The Sleek interface provides methods for drawing, positioning, handling touch, loading/unloading and attaching/detaching. The SleekParent interface is an extension of the Sleek interface that adds methods for adding, removing and getting Sleek instance children of the SleekParent. With these simple rules you can create almost any UI hierarchy.

## The SleekCanvas class
The SleekCanvas behaves just like any other View in Android externally, meaning you can use it wherever you can use a `android.view.View`. Internally however it supports adding Sleek instances, as well as `android.view.View` instances but wrapped inside of a Sleek interface (classes for adding Android Views can be found in the `com.ztory.lib.sleek.base.androidui` package). It can be helpful to use SleekCanvas for adding regular Android Views if you have problems getting the standard android layout logic to play along with your very specific and demanding layout requirements.

The real power of SleekCanvas lies in the ability to supply Sleek instances to represent the UI (a number of base classes exists for this in the `com.ztory.lib.sleek.base` package) of an app. It features minimum overhead for drawing, offers a predictable and straight forward layout and positioning logic, as well as simple (yet 100% customizable) touch functionality.

## Code example
Setting up a basic SleekCanvas in your `Activity.onCreate()` with a [SleekElement](https://github.com/ztory/sleek/blob/master/sleek_module/src/main/java/com/ztory/lib/sleek/base/element/SleekElement.java) styled with CSS ([Supported CSS properties can be found here](https://github.com/ztory/sleek/blob/master/sleek_module/src/main/java/com/ztory/lib/sleek/base/element/css/CSSblockBase.java)) and a (random!) click-handler is as easy as this:
```java
@Override
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  SleekCanvas sleekCanvas = new SleekCanvas(this);
  sleekCanvas.setBackgroundColor(0xffeeeeee);
  sleekCanvas.setSleekScroller(new SleekScrollerXY(true, true));
  CSSblock elementCSS = new CSSblockBase("{background: #1FA350;color: #fff;font-size: 20px;}");
  CSSblock elementCSSpressed = new CSSblockBase("{background: #1F6F8D;color: #FFC27F;}");
  CSSblock elementCSSclicked = new CSSblockBase("{background: #E14B2B;color: #aaa;}");
  final SleekElement element = new SleekElement();
  element.setElementString("Element of the year");
  element.addCSS(elementCSS);
  element.setClickAction(
      elementCSSpressed,
      elementCSSclicked,
      new Assumeable<SleekCSSanim>() { @Override public void assume(SleekCSSanim animation) {
        animation.setGoalX(element.getSleekX() - 100 + (int) (200 * Math.random()));
        animation.setGoalY(element.getSleekY() - 100 + (int) (200 * Math.random()));
        animation.setDuration(SleekCSSanim.ANIM_DURATION_LONG);
      }}
  );
  element.getLayout()
      .x(SL.X.POS_CENTER, 0, null)
      .y(SL.Y.POS_CENTER, 0, null)
      .w(SL.W.ABSOLUTE, UtilPx.getPixels(this, 200), null)
      .h(SL.H.ABSOLUTE, UtilPx.getPixels(this, 200), null);
  sleekCanvas.addSleek(element);
  setContentView(sleekCanvas);
}
```

### Psst
Call this in your `Application.onCreate()` to have everything working as it should:
```java
public class MainApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    UtilPx.setDefaultContext(this);
  }
}
```

### Want more example code?
Pull down the repo and run the `TestSleekUI` test class for a quick demo. Explore the source code of `TestSleekUI` at your own risk... =)

## Enough! I want to use it, tell me how!

#### Step 1
In your base `gradle.build` file (the one in project root), add this:
```
maven { url "https://github.com/ztory/sleek/raw/master/maven-repository/" }
```
So that it will look something like this:
```
allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven { url "https://github.com/ztory/sleek/raw/master/maven-repository/" }
    }
}
```

#### Step 2
In your module `build.gradle` add this:
```
compile 'com.ztory.lib.sleek:sleek_module:1.2.1'
```

## Code Style
`NOTE:` Just started using this code style, so classes that were created before have not been reformatted yet!
- [Google Style Guides](https://github.com/google/styleguide)
  - [intellij-java-google-style.xml](https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml)

## What else?

If you create great stuff with the Sleek framework and think it would make a great addition to the library, well then just FORK and PR I'd say!

If you have any problems or suggestions just create one of those [GitHub Issues](https://github.com/ztory/sleek/issues)!

Happy coding! =]
