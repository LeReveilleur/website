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

### How to add a new video post?

Add an entry in `data/videos.yaml` following the spec. Then run the following script: `bb generate-video-posts`. The script will automatically generate the article in the folder `content/post` with the name `auto_generated__{$CREATION_DATE}_{$TITLE}`.
