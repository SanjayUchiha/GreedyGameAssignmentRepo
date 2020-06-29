app:-
    1. App fires https://www.reddit.com/r/images/hot.json API 
    2. Parses the response
    3. shows list of images from the response in recyclerview
    4. App has pull to refresh feature.
    5. if there is any error in api, then there is retry button a user can click in order to retry fetching the data
    6. FullScreenImageActivity will display the image when clicked on list

imageloader:-
    1. ImageCache class is responsible for maintaining the cache
    2. DownloadImageTask is responsible to fetching the image if the image is not in the cache.
    
