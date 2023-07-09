# Image Capture
Use the `ImageCapture` class to create an image from a location.
This location can be a player's eye location, or any other location.
The full location data is used, so make sure the yaw and pitch values are also correct.

This is a minimal example of using the `ImageCapture` class,
taking a picture from the player's eye location:
```java
public void takePicture(Player player) {
    ImageCapture capture = new ImageCapture(
        player.getEyeLocation()
    );
    
    // this image is the final image capture
    BufferedImage image = capture.render();
}
```

The last example doesn't render players, as it doesn't know there are any.
The following example puts the player list parameter in the constructor,
so that players will be visible:
```java
public void takePicture(Player player) {
    List<Player> worldPlayers = player.getWorld().getPlayers();
    // make sure to remove the player that's taking the picture
    // when you take a picture from the player's perspective
    worldPlayers.remove(player);

    ImageCapture capture =n ew ImageCapture(
        player.getEyeLocation(),
        worldPlayers
    );

    // this image is the final image capture
    BufferedImage image = capture.render();
}
```

The constructor is also able to take the `ImageCaptureOptions` class,
with which you can specify a lot of options for the image capture.
Here's an example:
```java
public void takePicture(Player player) {
    ImageCapture capture = new ImageCapture(
        player.getEyeLocation(),
        ImageCaptureOptions
            .builder()
            .fov(1f) // field of view
            .dayLightCycleAware(false) // toggle night-time sky color
            .build()
    );

    // this image is the final image capture
    BufferedImage image = capture.render();
}
```

These are all the possible options:

| Name               	| Description                                                                                	| Constraints                                                                                                                        	|
|--------------------	|--------------------------------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------------------------------	|
| width              	| The width of the resulting image.                                                          	| -                                                                                                                                  	|
| height             	| The height of the resulting image.                                                         	| -                                                                                                                                  	|
| fov                	| The field of view of the image.                                                            	| Should be a reasonable value near 1. Lower values are more zoomed in, higher values have a wider view. Examples would be 0.5 or 2. 	|
| skyColor           	| The color the sky has during day time.                                                     	| It has to be day time to see it, or dayLightCycleAware should be disabled.                                                         	|
| skyColorNight      	| The color the sky has during night time.                                                   	| It has to be night time to see it. Does not work at all with dayLightCycleAware disabled.                                          	|
| excludedBlocks     	| Blocks that the camera should look through.                                                	| Should reasonable only be an extended version of Constants.EXCLUDED_BLOCKS.                                                        	|
| translucentBlocks  	| Blocks that the camera should look through, but also add a filter to. (e.g. stained glass) 	| Constants.TRANSLUCENT_BLOCKS should be the starting point for changing this.                                                       	|
| blocks             	| A list of blocks that the camera can use to color the image.                               	| Constants.BLOCKS should be the starting point for changing this.                                                                   	|
| dayLightCycleAware 	| Whether to change the sky color depending on the world time.                               	| -                                                                                                                                  	|

## BIG WARNING
The render method can be very slow depending on the scene and the server processor.
It could even take 1-2 seconds to render a complete image.
Because of this, **you should always call this method asynchronously**.
You don't want your server to freeze while the image is rendering.
Here's an example:
```java
public void takePicture(Player player) {
    ImageCapture capture = new ImageCapture(
        player.getEyeLocation()
    );
    
    new BukkitRunnable(){
        @Override
        public void run(){
            BufferedImage image = capture.render();
            // do something with the image
        }
    }.runTaskAsynchronously(<plugin instance>);
}
```
