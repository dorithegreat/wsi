import numpy as np
# shows as error importing but it works
import tensorflow.keras as keras
from tensorflow.keras.datasets import mnist
from keras.utils import to_categorical
from keras.models import Sequential
from keras.layers import Dense, Input

from sklearn.metrics import confusion_matrix
from PIL import Image

import matplotlib.pyplot as plt
import cv2
import os
import glob

(training_images, training_labels), (test_images, test_labels) = mnist.load_data()

# print(training_images.shape)
# print(test_images.shape)

# plt.imshow(training_images[0], cmap="gray")
# plt.show()
# print(type(training_images[0]))   

img_training = training_images / 255
img_test = test_images / 255

lbl_train = to_categorical(training_labels, 10)
lbl_test = to_categorical(test_labels, 10)

num_train, rows, columns = img_training.shape
img_training = img_training.reshape(num_train, rows * columns)

model = Sequential([
    Input(shape = (rows * columns,)),
    Dense(128, activation = "relu"),
    Dense(10, activation = "softmax")
])

model.compile(optimizer="adam", loss="categorical_crossentropy", metrics = [
    keras.metrics.Accuracy()
    # keras.metrics.FalsePositives(),
    # keras.metrics.FalseNegatives(),
    # keras.metrics.TruePositives(),
    # keras.metrics.TrueNegatives()
    ])
history = model.fit(img_training, lbl_train, epochs = 8, batch_size = 32)

num_test, _, _ = img_test.shape
img_test = img_test.reshape(num_test, rows * columns)
evaluation = model.evaluate(img_test, lbl_test)
print(evaluation)

predictions = model.predict(img_test)
prediction_index = predictions.argmax(1)
print(prediction_index)
# print(test_labels[:20])

cm_labels = np.argmax(lbl_test, axis=1)
cm = confusion_matrix(y_true=cm_labels, y_pred=np.argmax(predictions, axis=1))
print(cm)

true_positive = np.diag(cm)
false_positive = []
for i in range(10):
    false_positive.append(sum(cm[:,i]) - cm[i,i])
false_negative = []
for i in range(10):
    false_negative.append(sum(cm[i,:]) - cm[i,i])
true_negative = []
# everything except for ith row and ith column
for i in range(10):
    temp = np.delete(cm, i, 0)
    temp = np.delete(temp, i, 1)
    true_negative.append(sum(sum(temp)))

print(true_positive + false_negative + false_positive + true_negative)
print("true positive: ", true_positive)
print("true negative: ", true_negative)
print("false positive: ", false_positive)
print("false negative: ", false_negative)
print("\n")

print("\nHistory \n")
print(history.history.keys())
print(history.history['accuracy'])
# print(history.history['false_negatives'])

# print(model.evaluate(img_test, lbl_test))

own_images = []
order = []

# why the fuck is it not in order
for file in os.listdir("."):
    order.append(file)

    test_image = cv2.imread(file, cv2.IMREAD_GRAYSCALE)
    img_resized = cv2.resize(test_image, (28, 28), interpolation=cv2.INTER_LINEAR)
    plt.imshow(img_resized)
    plt.show()

    own_images.append(img_resized)
    

images = np.array(own_images, dtype=np.uint8)
images = images / 255.0
    
num_test, _, _ = images.shape
images = images.reshape(num_test, rows * columns)

# model.predict(images)
# own_labels = [8,8,8,5,5,5,4,4,4,9,9,9,1,1,1,7,7,7,6,6,6,3,3,3,2,2,2,1,1,1]
# model.predict(own_images)

predictions = model.predict(images)
prediction_index = predictions.argmax(1)
print(prediction_index)
print(order)

# 5 8 3 3 2 5 2 8 3 3 3 7 6 2 5 6 7 3 3 5 2 2 2 7 4 4 7 9 7 5
# 5 6 3 9 2 8 1 8 3 1 3 7 6 1 6 8 0 9 0 5 0 2 2 7 4 4 9 4 7 5 
# x   x   x     x x   x x x             x   x x x x x     x x