# Skript integration
This plugin includes a Skript integration.
This enables you to call the ImageCapture API via Skript.
There are some prerequisites to using the Skript integration:
- Skript should be installed (tested on v2.6.4)
- A storage provider should be configured

When you have done these things,
you can start writing scripts using ImageCapture.

## Examples
A few examples of how to use the integration.
These examples use Skellett to write the image to a map.

### Write an existing image to a map when it's initialized
```
on map:
    set {_map} to event-map
    manage skellett map {_map}
    # this uuid is hardcoded right here, but it should (of course) exist in storage
    set {_image} to image from storage with uuid "323ca677-8902-4f2a-90cd-239f773a86bb"
    draw image image from {_image} on skellett map {_map}
    loop all players:
        send map {_map} to loop-player
```

### Take a picture when a command is executed
```
command /takepicture:
    trigger:
        set {_map} to a new map from player's world
        manage skellett map {_map}
        # passing all players will render all players in the view 
        take picture from player's eye location and set to {_image}
        draw image image from {_image} on skellett map {_map}
        loop all players:
            send map {_map} to loop-player
        execute console command "/minecraft:give %name of player% minecraft:filled_map{map:%id of {_map}%} 1"
```

### Write an image capture with options to a map when it's initialized
```
command /takepicture:
    trigger:
        set {_map} to a new map from player's world
        manage skellett map {_map}
        # passing all players will render all players in the view 
        set {_options} to capture options fov 0.5 day light cycle aware false
    set {_image} to image taken from player's eye location with options {_options}
        draw image image from {_image} on skellett map {_map}
        loop all players:
            send map {_map} to loop-player
        execute console command "/minecraft:give %name of player% minecraft:filled_map{map:%id of {_map}%} 1"
```

### Write an image capture with player renders to a map when it's initialized
```
command /takepicture:
    trigger:
        set {_map} to a new map from player's world
        manage skellett map {_map}
        # passing all players will render all players in the view 
    set {_image} to image taken from player's eye location with players all players
        draw image image from {_image} on skellett map {_map}
        loop all players:
            send map {_map} to loop-player
        execute console command "/minecraft:give %name of player% minecraft:filled_map{map:%id of {_map}%} 1"
```

## Reference
The integration consists of some effects and expressions.
There is also 2 additional data types.

### Effects
`take picture from %location% [with options %-imagecaptureconfig%] [with players %players%] and set to %-object%`
is the recommended way to take pictures using this addon.
Takes the location from which to take the image from,
and some optional options.
It takes the picture asynchronously to the main thread
and puts the result inside the variable you specify in the last argument.

`remove image %storedimage% from storage`
removes an image from storage using a storedimage-typed variable as a reference.

`remove image %string% from storage`
removes an image from storage using a string (uuid format) as a reference.

### Expressions
`[image] from storage with uuid %string%`
fetches an image from storage, identified by the uuid, given as a string.

`image (from|of) %storedimage%`
returns the actual image of the storedimage data type.
Use this when you want to input the image into some other addon to render it.

`capture options [fov %-float%] [day light cycle aware %-boolean%]`
returns a configuration to use inside the "take picture" effect.

`uuid (from|of) %storedimage%`
returns the uuid of the stored image.
Can be used to store a reference of your image inside your own storage.
Note: saving the UUID is the only "correct" way to fetch a specific image.

`[image] taken from %location% [with options %-imagecaptureconfig%] [with players %players%]`
returns an image taken from the given location.
This works the same way as the effect, BUT IT'S NOT ASYNC.
Please be aware of this when using this expression,
as it may freeze your server.

`id of %map%`
Used to get the id from a mapview.
This is necessary because Skellett (recommended for writing maps) doesn't export this in any way.
Use this id to create filled_map items.

## Data types
**StoredImage** represents an image that is also stored in storage.
Taking pictures returns this method.

**ImageCaptureOptions** represents a set of options for an image capture.