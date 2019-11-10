# USAGE
# python text_recognition.py --east frozen_east_text_detection.pb --image images/example_01.jpg
# python text_recognition.py --east frozen_east_text_detection.pb --image images/example_04.jpg --padding 0.05

# import the necessary packages
from imutils.object_detection import non_max_suppression
from TOOLS import Functions

import cv2
import numpy as np
import math
import argparse
import numpy as np
import pytesseract
import argparse
import cv2

pytesseract.pytesseract.tesseract_cmd = r'tesseract/tesseract.exe'

def decode_predictions(scores, geometry):
	# grab the number of rows and columns from the scores volume, then
	# initialize our set of bounding box rectangles and corresponding
	# confidence scores
	(numRows, numCols) = scores.shape[2:4]
	rects = []
	confidences = []

	# loop over the number of rows
	for y in range(0, numRows):
		# extract the scores (probabilities), followed by the
		# geometrical data used to derive potential bounding box
		# coordinates that surround text
		scoresData = scores[0, 0, y]
		xData0 = geometry[0, 0, y]
		xData1 = geometry[0, 1, y]
		xData2 = geometry[0, 2, y]
		xData3 = geometry[0, 3, y]
		anglesData = geometry[0, 4, y]

		# loop over the number of columns
		for x in range(0, numCols):
			# if our score does not have sufficient probability,
			# ignore it
			if scoresData[x] < 0.5:
				continue

			# compute the offset factor as our resulting feature
			# maps will be 4x smaller than the input image
			(offsetX, offsetY) = (x * 4.0, y * 4.0)

			# extract the rotation angle for the prediction and
			# then compute the sin and cosine
			angle = anglesData[x]
			cos = np.cos(angle)
			sin = np.sin(angle)

			# use the geometry volume to derive the width and height
			# of the bounding box
			h = xData0[x] + xData2[x]
			w = xData1[x] + xData3[x]

			# compute both the starting and ending (x, y)-coordinates
			# for the text prediction bounding box
			endX = int(offsetX + (cos * xData1[x]) + (sin * xData2[x]))
			endY = int(offsetY - (sin * xData1[x]) + (cos * xData2[x]))
			startX = int(endX - w)
			startY = int(endY - h)

			# add the bounding box coordinates and probability score
			# to our respective lists
			rects.append((startX, startY, endX, endY))
			confidences.append(scoresData[x])

	# return a tuple of the bounding boxes and associated confidences
	return (rects, confidences)

# load the pre-trained EAST text detector
print("[INFO] loading EAST text detector...")
net = cv2.dnn.readNet("frozen_east_text_detection.pb")

def getNum(image):
        # construct the argument parser and parse the arguments
        ap = argparse.ArgumentParser()
        ap.add_argument("-i", "--image", type=str,
                help="path to input image")
        ap.add_argument("-east", "--east", type=str,
                help="path to input EAST text detector")
        ap.add_argument("-c", "--min-confidence", type=float, default=0.5,
                help="minimum probability required to inspect a region")
        ap.add_argument("-w", "--width", type=int, default=320,
                help="nearest multiple of 32 for resized width")
        ap.add_argument("-e", "--height", type=int, default=320,
                help="nearest multiple of 32 for resized height")
        ap.add_argument("-p", "--padding", type=float, default=0.0,
                help="amount of padding to add to each border of ROI")
        args = vars(ap.parse_args())

        # load the input image and grab the image dimensions
        #image = cv2.imread("images/example_04.jpg")
        orig = image.copy()
        (origH, origW) = image.shape[:2]

        # set the new width and height and then determine the ratio in change
        # for both the width and height
        (newW, newH) = (args["width"], args["height"])
        rW = origW / float(newW)
        rH = origH / float(newH)

        # resize the image and grab the new image dimensions
        image = cv2.resize(image, (newW, newH))
        (H, W) = image.shape[:2]

        # define the two output layer names for the EAST detector model that
        # we are interested -- the first is the output probabilities and the
        # second can be used to derive the bounding box coordinates of text
        layerNames = [
                "feature_fusion/Conv_7/Sigmoid",
                "feature_fusion/concat_3"]


        # construct a blob from the image and then perform a forward pass of
        # the model to obtain the two output layer sets
        blob = cv2.dnn.blobFromImage(image, 1.0, (W, H),
                (123.68, 116.78, 103.94), swapRB=True, crop=False)
        net.setInput(blob)
        (scores, geometry) = net.forward(layerNames)

        # decode the predictions, then  apply non-maxima suppression to
        # suppress weak, overlapping bounding boxes
        (rects, confidences) = decode_predictions(scores, geometry)
        boxes = non_max_suppression(np.array(rects), probs=confidences)

        # initialize the list of results
        results = []

        # loop over the bounding boxes
        for (startX, startY, endX, endY) in boxes:
                # scale the bounding box coordinates based on the respective
                # ratios
                startX = int(startX * rW)
                startY = int(startY * rH)
                endX = int(endX * rW)
                endY = int(endY * rH)

                # in order to obtain a better OCR of the text we can potentially
                # apply a bit of padding surrounding the bounding box -- here we
                # are computing the deltas in both the x and y directions
                dX = int((endX - startX) * args["padding"])
                dY = int((endY - startY) * args["padding"])

                # apply padding to each side of the bounding box, respectively
                startX = max(0, startX - dX)
                startY = max(0, startY - dY)
                endX = min(origW, endX + (dX * 2))
                endY = min(origH, endY + (dY * 2))

                # extract the actual padded ROI
                roi = orig[startY:endY, startX:endX]

                # in order to apply Tesseract v4 to OCR text we must supply
                # (1) a language, (2) an OEM flag of 4, indicating that the we
                # wish to use the LSTM neural net model for OCR, and finally
                # (3) an OEM value, in this case, 7 which implies that we are
                # treating the ROI as a single line of text
                config = ("-l eng --oem 1 --psm 7")
                text = pytesseract.image_to_string(roi, config=config)

                # add the bounding box coordinates and OCR'd text to the list
                # of results
                results.append(((startX, startY, endX, endY), text))

        # sort the results bounding box coordinates from top to bottom
        results = sorted(results, key=lambda r:r[0][1])

        # loop over the results
        textString = ""
        for ((startX, startY, endX, endY), text) in results:
                # display the text OCR'd by Tesseract
                #print("OCR TEXT")
                #print("========")
                #print("{}\n".format(text))
                for i in text:
                        if i.isalnum():
                                textString+=i
                # strip out non-ASCII text so we can draw the text on the image
                # using OpenCV, then draw the text and a bounding box surrounding
                # the text region of the input image
                #text = "".join([c if ord(c) < 128 else "" for c in text]).strip()
                #output = orig.copy()
                #cv2.rectangle(output, (startX, startY), (endX, endY),	(0, 0, 255), 2)
                #cv2.putText(output, text, (startX, startY - 20),		cv2.FONT_HERSHEY_SIMPLEX, 1.2, (0, 0, 255), 3)

                # show the output image
                #cv2.imshow("Text Detection", output)
                #cv2.waitKey(0)

        print(textString)


def getPlate(img):

        #img = cv2.imread(args["image"])
        # cv2.imshow('original', img)
        # cv2.imwrite(temp_folder + '1 - original.png', img)

        # hsv transform - value = gray image
        hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        hue, saturation, value = cv2.split(hsv)
        # cv2.imshow('gray', value)
        # cv2.imwrite(temp_folder + '2 - gray.png', value)

        # kernel to use for morphological operations
        kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3))

        # applying topHat/blackHat operations
        topHat = cv2.morphologyEx(value, cv2.MORPH_TOPHAT, kernel)
        blackHat = cv2.morphologyEx(value, cv2.MORPH_BLACKHAT, kernel)
        # cv2.imshow('topHat', topHat)
        # cv2.imshow('blackHat', blackHat)
        # cv2.imwrite(temp_folder + '3 - topHat.png', topHat)
        # cv2.imwrite(temp_folder + '4 - blackHat.png', blackHat)

        # add and subtract between morphological operations
        add = cv2.add(value, topHat)
        subtract = cv2.subtract(add, blackHat)
        # cv2.imshow('subtract', subtract)
        # cv2.imwrite(temp_folder + '5 - subtract.png', subtract)

        # applying gaussian blur on subtract image
        blur = cv2.GaussianBlur(subtract, (5, 5), 0)
        # cv2.imshow('blur', blur)
        # cv2.imwrite(temp_folder + '6 - blur.png', blur)

        # thresholding
        thresh = cv2.adaptiveThreshold(blur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 19, 9)
        # cv2.imshow('thresh', thresh)
        # cv2.imwrite(temp_folder + '7 - thresh.png', thresh)

        # cv2.findCountours() function changed from OpenCV3 to OpenCV4: now it have only two parameters instead of 3
        cv2MajorVersion = cv2.__version__.split(".")[0]
        # check for contours on thresh
        if int(cv2MajorVersion) >= 4:
            contours, hierarchy = cv2.findContours(thresh, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
        else:
            imageContours, contours, hierarchy = cv2.findContours(thresh, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)

        # get height and width
        height, width = thresh.shape

        # create a numpy array with shape given by threshed image value dimensions
        imageContours = np.zeros((height, width, 3), dtype=np.uint8)

        # list and counter of possible chars
        possibleChars = []
        countOfPossibleChars = 0

        # loop to check if any (possible) char is found
        for i in range(0, len(contours)):

            # draw contours based on actual found contours of thresh image
            cv2.drawContours(imageContours, contours, i, (255, 255, 255))

            # retrieve a possible char by the result ifChar class give us
            possibleChar = Functions.ifChar(contours[i])

            # by computing some values (area, width, height, aspect ratio) possibleChars list is being populated
            if Functions.checkIfChar(possibleChar) is True:
                countOfPossibleChars = countOfPossibleChars + 1
                possibleChars.append(possibleChar)

        # cv2.imshow("contours", imageContours)
        # cv2.imwrite(temp_folder + '8 - imageContours.png', imageContours)

        imageContours = np.zeros((height, width, 3), np.uint8)

        ctrs = []

        # populating ctrs list with each char of possibleChars
        for char in possibleChars:
            ctrs.append(char.contour)

        # using values from ctrs to draw new contours
        cv2.drawContours(imageContours, ctrs, -1, (255, 255, 255))
        # cv2.imshow("contoursPossibleChars", imageContours)
        # cv2.imwrite(temp_folder + '9 - contoursPossibleChars.png', imageContours)

        plates_list = []
        listOfListsOfMatchingChars = []

        for possibleC in possibleChars:

            # the purpose of this function is, given a possible char and a big list of possible chars,
            # find all chars in the big list that are a match for the single possible char, and return those matching chars as a list
            def matchingChars(possibleC, possibleChars):
                listOfMatchingChars = []

                # if the char we attempting to find matches for is the exact same char as the char in the big list we are currently checking
                # then we should not include it in the list of matches b/c that would end up double including the current char
                # so do not add to list of matches and jump back to top of for loop
                for possibleMatchingChar in possibleChars:
                    if possibleMatchingChar == possibleC:
                        continue

                    # compute stuff to see if chars are a match
                    distanceBetweenChars = Functions.distanceBetweenChars(possibleC, possibleMatchingChar)

                    angleBetweenChars = Functions.angleBetweenChars(possibleC, possibleMatchingChar)

                    changeInArea = float(abs(possibleMatchingChar.boundingRectArea - possibleC.boundingRectArea)) / float(
                        possibleC.boundingRectArea)

                    changeInWidth = float(abs(possibleMatchingChar.boundingRectWidth - possibleC.boundingRectWidth)) / float(
                        possibleC.boundingRectWidth)

                    changeInHeight = float(abs(possibleMatchingChar.boundingRectHeight - possibleC.boundingRectHeight)) / float(
                        possibleC.boundingRectHeight)

                    # check if chars match
                    if distanceBetweenChars < (possibleC.diagonalSize * 5) and \
                            angleBetweenChars < 12.0 and \
                            changeInArea < 0.5 and \
                            changeInWidth < 0.8 and \
                            changeInHeight < 0.2:
                        listOfMatchingChars.append(possibleMatchingChar)

                return listOfMatchingChars


            # here we are re-arranging the one big list of chars into a list of lists of matching chars
            # the chars that are not found to be in a group of matches do not need to be considered further
            listOfMatchingChars = matchingChars(possibleC, possibleChars)

            listOfMatchingChars.append(possibleC)

            # if current possible list of matching chars is not long enough to constitute a possible plate
            # jump back to the top of the for loop and try again with next char
            if len(listOfMatchingChars) < 3:
                continue

            # here the current list passed test as a "group" or "cluster" of matching chars
            listOfListsOfMatchingChars.append(listOfMatchingChars)

            # remove the current list of matching chars from the big list so we don't use those same chars twice,
            # make sure to make a new big list for this since we don't want to change the original big list
            listOfPossibleCharsWithCurrentMatchesRemoved = list(set(possibleChars) - set(listOfMatchingChars))

            recursiveListOfListsOfMatchingChars = []

            for recursiveListOfMatchingChars in recursiveListOfListsOfMatchingChars:
                listOfListsOfMatchingChars.append(recursiveListOfMatchingChars)

            break

        imageContours = np.zeros((height, width, 3), np.uint8)

        for listOfMatchingChars in listOfListsOfMatchingChars:
            contoursColor = (255, 0, 255)

            contours = []

            for matchingChar in listOfMatchingChars:
                contours.append(matchingChar.contour)

            cv2.drawContours(imageContours, contours, -1, contoursColor)

        # cv2.imshow("finalContours", imageContours)
        # cv2.imwrite(temp_folder + '10 - finalContours.png', imageContours)

        for listOfMatchingChars in listOfListsOfMatchingChars:
            possiblePlate = Functions.PossiblePlate()

            # sort chars from left to right based on x position
            listOfMatchingChars.sort(key=lambda matchingChar: matchingChar.centerX)

            # calculate the center point of the plate
            plateCenterX = (listOfMatchingChars[0].centerX + listOfMatchingChars[len(listOfMatchingChars) - 1].centerX) / 2.0
            plateCenterY = (listOfMatchingChars[0].centerY + listOfMatchingChars[len(listOfMatchingChars) - 1].centerY) / 2.0

            plateCenter = plateCenterX, plateCenterY

            # calculate plate width and height
            plateWidth = int((listOfMatchingChars[len(listOfMatchingChars) - 1].boundingRectX + listOfMatchingChars[
                len(listOfMatchingChars) - 1].boundingRectWidth - listOfMatchingChars[0].boundingRectX) * 1.3)

            totalOfCharHeights = 0

            for matchingChar in listOfMatchingChars:
                totalOfCharHeights = totalOfCharHeights + matchingChar.boundingRectHeight

            averageCharHeight = totalOfCharHeights / len(listOfMatchingChars)

            plateHeight = int(averageCharHeight * 1.5)

            # calculate correction angle of plate region
            opposite = listOfMatchingChars[len(listOfMatchingChars) - 1].centerY - listOfMatchingChars[0].centerY

            hypotenuse = Functions.distanceBetweenChars(listOfMatchingChars[0],
                                                        listOfMatchingChars[len(listOfMatchingChars) - 1])
            correctionAngleInRad = math.asin(opposite / hypotenuse)
            correctionAngleInDeg = correctionAngleInRad * (180.0 / math.pi)

            # pack plate region center point, width and height, and correction angle into rotated rect member variable of plate
            possiblePlate.rrLocationOfPlateInScene = (tuple(plateCenter), (plateWidth, plateHeight), correctionAngleInDeg)

            # get the rotation matrix for our calculated correction angle
            rotationMatrix = cv2.getRotationMatrix2D(tuple(plateCenter), correctionAngleInDeg, 1.0)

            height, width, numChannels = img.shape

            # rotate the entire image
            imgRotated = cv2.warpAffine(img, rotationMatrix, (width, height))

            # crop the image/plate detected
            imgCropped = cv2.getRectSubPix(imgRotated, (plateWidth, plateHeight), tuple(plateCenter))

            # copy the cropped plate image into the applicable member variable of the possible plate
            possiblePlate.Plate = imgCropped

            # populate plates_list with the detected plate
            if possiblePlate.Plate is not None:
                plates_list.append(possiblePlate)


        return plates_list
        """
            # draw a ROI on the original image
            p=[]
            for i in range(0, len(plates_list)):
                # finds the four vertices of a rotated rect - it is useful to draw the rectangle.
                p2fRectPoints = cv2.boxPoints(plates_list[i].rrLocationOfPlateInScene)

                # roi rectangle colour
                rectColour = (0, 255, 0)

                cv2.line(imageContours, tuple(p2fRectPoints[0]), tuple(p2fRectPoints[1]), rectColour, 2)
                cv2.line(imageContours, tuple(p2fRectPoints[1]), tuple(p2fRectPoints[2]), rectColour, 2)
                cv2.line(imageContours, tuple(p2fRectPoints[2]), tuple(p2fRectPoints[3]), rectColour, 2)
                cv2.line(imageContours, tuple(p2fRectPoints[3]), tuple(p2fRectPoints[0]), rectColour, 2)

                cv2.line(img, tuple(p2fRectPoints[0]), tuple(p2fRectPoints[1]), rectColour, 2)
                cv2.line(img, tuple(p2fRectPoints[1]), tuple(p2fRectPoints[2]), rectColour, 2)
                cv2.line(img, tuple(p2fRectPoints[2]), tuple(p2fRectPoints[3]), rectColour, 2)
                cv2.line(img, tuple(p2fRectPoints[3]), tuple(p2fRectPoints[0]), rectColour, 2)

                #cv2.imshow("detected", imageContours)
                #cv2.imwrite(temp_folder + '11 - detected.png', imageContours)

                #cv2.imshow("detectedOriginal", img)
                #cv2.imwrite(temp_folder + '12 - detectedOriginal.png', img)

                #cv2.imshow("plate", plates_list[i].Plate)
                cv2.imwrite(temp_folder + '13 - plate.png', plates_list[i].Plate)
                
        """

# Function to extract frames 
def FrameCapture(path): 
      
    # Path to video file 
    vidObj = cv2.VideoCapture(path) 
  
    # Used as counter variable 
    count = 0
  
    # checks whether frames were extracted 
    success = 1
    while success: 
  
        # vidObj object calls read 
        # function extract frames 
        success, image = vidObj.read() 
  
        # Saves the frames with frame-count 
        #cv2.imwrite("frame%d.jpg" % count, image)
        if count%10==0:
            getNum(image)
        """        
        if count%20==0:
                plates_list = getPlate(image)
                cv2.imshow("plate", image)
                for i in range(0, len(plates_list)):
                        cv2.imshow("plate", plates_list[i].Plate)
                        #getNum(plates_list[i].Plate)
            
        """  
        count += 1
  
# Driver Code 
if __name__ == '__main__': 
  
    # Calling the function 
    #FrameCapture("video12.mp4") 
    #FrameCapture("video15.mp4") 
    FrameCapture("video.mov") 
    #FrameCapture("test.mp4") 


