# Sleek

Sleek is an Android UI framework that tries to simplify complex UI hierarchy in the Android world.

## Features
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
Setting up a basic SleekCanvas with a clickable area that has a custom (and random!) touch functionality, in your `Activity.onCreate()`:
```java
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    SleekCanvas sleekCanvas = new SleekCanvas(this);
    sleekCanvas.setBackgroundColor(Color.BLUE);
    sleekCanvas.setSleekScroller(new SleekScrollerBase(true));
    final SleekColorArea sleekColorArea = new SleekColorArea(
            Color.CYAN,
            SleekColorArea.ANTI_ALIASED_TRUE,
            SleekColorArea.FIXED_POSITION_TRUE,
            SleekColorArea.TOUCHABLE_TRUE,
            SleekColorArea.LOADABLE_FALSE,
            SleekColorArea.TOUCH_PRIO_DEFAULT
    );
    sleekColorArea.getLayout()
            .x(SL.X.PERCENT_CANVAS, 0, null, 0.33f)
            .y(SL.Y.PERCENT_CANVAS, 0, null, 0.29f)
            .w(SL.W.PERCENT_CANVAS, 0, null, 0.27f)
            .h(SL.H.PERCENT_CANVAS, 0, null, 0.34f);
    int pixelsFromDip = UtilPx.getPixels(this, 8);// 8 DIP converted to pixels
    sleekColorArea.setRounded(true, pixelsFromDip);
    sleekColorArea.getTouchHandler().setClickAction(
            new Runnable() {
                @Override
                public void run() {
                    sleekColorArea.setColor(Color.MAGENTA);
                }
            },
            new Runnable() {
                @Override
                public void run() {
                    sleekColorArea.setColor(Color.CYAN);
                }
            },
            new Runnable() {
                @Override
                public void run() {
                    float randomGoalX = sleekColorArea.getSleekX();
                    randomGoalX = randomGoalX - 100 + (float) (200 * Math.random());
                    float randomGoalY = sleekColorArea.getSleekY();
                    randomGoalY = randomGoalY - 100 + (float) (200 * Math.random());
                    sleekColorArea.setSleekAnimView(
                            new SAVtransXY(
                                    sleekColorArea.getSleekX(),
                                    randomGoalX,
                                    sleekColorArea.getSleekY(),
                                    randomGoalY,
                                    300,
                                    ISleekDrawView.NO_DRAW
                            )
                    );
                }
            }
    );
    sleekCanvas.addSleek(sleekColorArea);
    setContentView(sleekCanvas);
}
```

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
compile 'com.ztory.lib.sleek:sleek_module:1.1.4'
```

## Code Style
`NOTE:` Just started using this code style, so classes that were created before have not been reformatted yet!
- [Google Style Guides](https://github.com/google/styleguide)
  - [intellij-java-google-style.xml](https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml)

## What else?

If you create great stuff with the Sleek framework and think it would make a great addition to the library, well then just FORK and PR I'd say!

If you have any problems or suggestions just create one of those [GitHub Issues](https://github.com/ztory/sleek/issues)!

Happy coding! =]
