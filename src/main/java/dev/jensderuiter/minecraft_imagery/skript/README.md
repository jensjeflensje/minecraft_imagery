# Skript integration
This plugin includes a Skript integration.
This enables you to call the ImageCapture API via Skript.
There are some prerequisites to using the Skript integration:
- Skript should be installed (tested on v2.6.4)
- A storage provider should be configured

When you have done these things,
you can start writing scripts using ImageCapture.

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

## Data types
**StoredImage** represents an image that is also stored in storage.
Taking pictures returns this method.

**ImageCaptureOptions** represents a set of options for an image capture.