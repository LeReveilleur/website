# website

Hugo website for Le Réveilleur.

## Development

### Requirements

Install the following packages:
- [hugo](https://gohugo.io/)
- [babashka](https://github.com/babashka/babashka)
- [yt-dlp](https://github.com/yt-dlp/yt-dlp#thumbnail-options)

### Tasks

To get a list of all available tasks, run `bb tasks`.

### How to generate the donors page?

Save the list of all donors as a newline separated file and copy it under `/scripts/donors.txt`.
Then run the following tasks:
```
bb generate-donors-page
```

The content page located at `content/page/donors/index.md` will be updated to include the list of donors.

### How to add a new video post?

Add an entry in `data/videos.yaml` following the spec. 
Then run the following script: `bb generate-video-posts`. The script will automatically generate the article in the folder `content/post` with the name `auto_generated__{$CREATION_DATE}_{$TITLE}`.

### How to add the bibliography sources for a video post?

1. create the following directory `scripts/data/video/source/{$youtube_id}` using the `youtube_id` of the video.
2. Create an `index.md` file that can contain markdown content for the bibliography section
3. If assets are needed, create a folder `scripts/data/video/source/{$youtube_id}/bibliography/` and put them under this folder.
