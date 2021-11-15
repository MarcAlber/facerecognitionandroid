import numpy as np
import cv2
from PIL import Image
import base64
import io
import face_recognition

matching_images = []
images_to_compare = []
images_array = []
img_str = ""
titles = ""
firebase_url = ""

def main(data, array, original):
    decoded_data = base64.b64decode(data)
    images_array.append(array)
    images_to_compare.append(array)
    titles = array
    firebase_url = original

    np_data = np.fromstring(decoded_data, np.uint8)
    img = cv2.imdecode(np_data, cv2.IMREAD_UNCHANGED)
    img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

    pil_im = Image.fromarray(img_rgb)

    buff = io.BytesIO()
    pil_im.save(buff, format="PNG")

    rgb_img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    img_encoding = face_recognition.face_encodings(rgb_img)[0]

    decoded_data = base64.b64decode(titles)
    np_data = np.fromstring(decoded_data, np.uint8)
    img2 = cv2.imdecode(np_data,cv2.IMREAD_UNCHANGED)

    rgb_img2 = cv2.cvtColor(img2, cv2.COLOR_BGR2RGB)
    img_encoding2 = face_recognition.face_encodings(rgb_img2)[0]
    result = face_recognition.compare_faces([img_encoding], img_encoding2)

    if str(result) == "[True]":
        img_str = firebase_url

    else:
        print("FALSE")
        img_str = "---"
        #img_str = "https://cdn.pixabay.com/photo/2012/04/12/20/12/x-30465_960_720.png"
        # print("FALSE")

    return img_str
