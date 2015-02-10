#include <iostream>
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdio.h>
#include <unistd.h> /* close */
#include <string.h>
#include <stdlib.h>

using namespace cv;
using namespace std;

#define SERVER_PORT 50000

const char *HOST = "192.168.0.114";
int PORT = 50000;

void send(const char * cmd) {
    int client;  /* client socket */
    int rc;
    struct sockaddr_in local_addr, serv_addr;
    struct hostent * host;
    char message[6] = {'/','T','I','M','E','\n'};
    char date[25];


    /* get host address from specified server name */
    host = gethostbyname("192.168.0.114");

    if (host == NULL)
    {
        printf("%s: unknown host '%s'\n", HOST);
        exit(-1);
    }

    /* now fill in sockaddr_in for remote address */
    serv_addr.sin_family = host->h_addrtype;
    /* get first address in host, copy to serv_addr */
    memcpy((char *) &serv_addr.sin_addr.s_addr, host->h_addr_list[0], host->h_length);
    serv_addr.sin_port = htons(SERVER_PORT);
    memset(serv_addr.sin_zero, 0, 8);

    /* create local stream socket */
    client = socket(PF_INET, SOCK_STREAM, 0);
    if (client < 0) {
        perror("cannot open socket ");
        exit(-1);
    }

    /* bind local socket to any port number */
    local_addr.sin_family = AF_INET;
    local_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    local_addr.sin_port = htons(0);
    memset(local_addr.sin_zero, 0, 8);

    rc = bind(client, (struct sockaddr *) &local_addr, sizeof(local_addr));

    if (rc < 0)
    {
        printf("%s: cannot bind port TCP %u\n",HOST,SERVER_PORT);
        perror("error ");
        exit(1);
    }

    /* connect to server */
    rc = connect(client, (struct sockaddr *) &serv_addr, sizeof(serv_addr));
    if (rc < 0)
    {
        perror("cannot connect ");
        exit(1);
    }

    /* now send /TIME */
    rc = send(client, cmd, 1, 0);
    cout << "Send: " << cmd[0] << endl;
    if (rc < 0)
    {
        perror("cannot send data ");
        close(client);
        exit(-1);
    }

    /* we're expecting 25 chars from server, */
    read(client,date,25);

    printf(date);

    close(client);
}

int main(int argc, char **argv) {
    VideoCapture cap(0); //capture the video from webcam

    if (!cap.isOpened())  // if not success, exit program
    {
        cout << "Cannot open the web cam" << endl;
        return -1;
    }

    namedWindow("Control", WINDOW_AUTOSIZE); //create a window called "Control"

    int iLowH = 170;
    int iHighH = 179;

    int iLowS = 150;
    int iHighS = 255;

    int iLowV = 60;
    int iHighV = 255;

    //Create trackbars in "Control" window
    createTrackbar("LowH", "Control", &iLowH, 179); //Hue (0 - 179)
    createTrackbar("HighH", "Control", &iHighH, 179);

    createTrackbar("LowS", "Control", &iLowS, 255); //Saturation (0 - 255)
    createTrackbar("HighS", "Control", &iHighS, 255);

    createTrackbar("LowV", "Control", &iLowV, 255);//Value (0 - 255)
    createTrackbar("HighV", "Control", &iHighV, 255);

    int iLastX = -1;
    int iLastY = -1;

    //Capture a temporary image from the camera
    Mat imgTmp;
    cap.read(imgTmp);

    //Create a black image with the size as the camera output
    Mat imgLines = Mat::zeros(imgTmp.size(), CV_8UC3);;
    int a,b,d,e = 0;
    while (true) {
        Mat imgOriginal;

        bool bSuccess = cap.read(imgOriginal); // read a new frame from video



        if (!bSuccess) //if not success, break loop
        {
            cout << "Cannot read a frame from video stream" << endl;
            break;
        }

        Mat imgHSV;

        cvtColor(imgOriginal, imgHSV, COLOR_BGR2HSV); //Convert the captured frame from BGR to HSV

        Mat imgThresholded;

        inRange(imgHSV, Scalar(iLowH, iLowS, iLowV), Scalar(iHighH, iHighS, iHighV), imgThresholded); //Threshold the image

        //morphological opening (removes small objects from the foreground)
        erode(imgThresholded, imgThresholded, getStructuringElement(MORPH_ELLIPSE, Size(5, 5)));
        dilate(imgThresholded, imgThresholded, getStructuringElement(MORPH_ELLIPSE, Size(5, 5)));

        //morphological closing (removes small holes from the foreground)
        dilate(imgThresholded, imgThresholded, getStructuringElement(MORPH_ELLIPSE, Size(5, 5)));
        erode(imgThresholded, imgThresholded, getStructuringElement(MORPH_ELLIPSE, Size(5, 5)));

        //Calculate the moments of the thresholded image
        Moments oMoments = moments(imgThresholded);

        double dM01 = oMoments.m01;
        double dM10 = oMoments.m10;
        double dArea = oMoments.m00;

        // if the area <= 10000, I consider that the there are no object in the image and it's because of the noise, the area is not zero

        if (dArea > 10000) {
            //calculate the position of the ball
            int posX = dM10 / dArea;
            int posY = dM01 / dArea;

            if (iLastX >= 0 && iLastY >= 0 && posX >= 0 && posY >= 0) {
                //Draw a red line from the previous point to the current point
                line(imgLines, Point(posX, posY), Point(iLastX, iLastY), Scalar(0, 0, 255), 2);
            }
            if (iLastX != -1 and iLastX < posX) {
                char * c = new char [1];
                c[0] = 'l';
                if ( a > 10) {
                    for (int i = 0; i < 10; i++)
                        send(c);
                    a = 0;
                }
                a++;

            } else if (iLastX != -1 and iLastX > posX) {
                char * c = new char [1];
                c[0] = 'l';
                if (b > 10) {
                    for (int i = 0; i < 30; i++)
                        send(c);
                    b = 0;
                }
                b++;
            }
            if (iLastY != -1 and iLastY > posY) {
                char * c = new char [1];
                c[0] = 'i';
                if ( d > 10){
                    for (int i =0 ;i < 10; i++)
                    send(c);
                    d = 0;
                }
                d++;

            } else if (iLastY != -1 and iLastY < posY) {
                char * c = new char [1];
                c[0] = 'j';
                if (e > 10) {
                    for (int i = 0; i < 10; i++)
                        send(c);
                    e = 0;
                }
                e++;
            }
            sleep(0.5);
            cout << "\n";
            iLastX = posX;
            iLastY = posY;
        }

        imshow("Thresholded Image", imgThresholded); //show the thresholded image

        imgOriginal = imgOriginal + imgLines;
        imshow("Original", imgOriginal); //show the original image

        if (waitKey(30) == 27) //wait for 'esc' key press for 30ms. If 'esc' key is pressed, break loop
        {
            cout << "esc key is pressed by user" << endl;
            break;
        }
    }

    return 0;
}