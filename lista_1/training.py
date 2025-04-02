import numpy as np
# shows as error importing but it works
import tensorflow.keras as keras
from tensorflow.keras.datasets import mnist

import matplotlib.pyplot as plt

(training_images, training_labels), (test_images, test_labels) = mnist.load_data()

print(training_images.shape)
print(test_images.shape)

# plt.imshow(training_images[0], cmap="gray")
# plt.show()

img_training = training_images / 255
img_test = test_images / 255
