# facerecognitionandroid
A test app utilizing Firebase Services and Chaquopy, a Python SDK for using Python code into Android applications.
This app specifically uses Firebase Storage and the face_recognition library from Python.
First, you upload an image form your gallery. Then, it downloads all the images from Firebase Storage, put all the images into an ArrayList<> object.
Then, for each item in that ArrayList<>, it scans it with the uploaded photo to check if the faces match. If they do, return them to a new ArrayList<>.
Once all matching images are acquired, the Glide library loads them into a RecyclerView. To find other people that are in the image, just click that image.
A new activity will show up. Click that image, then a CropView will be enabled and then you can cropt ot the particular face you wish to look for.
