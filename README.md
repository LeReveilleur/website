# website

Hugo website for Le Réveilleur

## Deploy

Pushing code to the main github branch will trigger a
[netlify](https://www.netlify.com/) build which is configured in
[this config file](./netlify.toml)

## Development

### Requirements

Install the following packages:

- [hugo](https://gohugo.io/): Static Site Generator used for this webiste.
- [babashka](https://github.com/babashka/babashka): leverage Clojure in places
  where you would be using bash otherwise.
- [yt-dlp](https://github.com/yt-dlp/yt-dlp#thumbnail-options): used to
  automatically download youtube thumbnails to display in the articles.

### Run the hugo website locally

Run the following command to run the hugo website locally:

```sh
hugo server -D
```

Navigate with your browser to the url displayed by the above command.
Eg. `http://localhost:1313/`

### Tasks

To get a list of all available tasks, run `bb tasks`.

### How to generate the donors page?

Save the list of all donors as a newline separated file and copy it under
`/scripts/donors.txt`.
Then run the following task:

```sh
bb generate-donors-page
```

The content page located at `content/page/donors/index.md` will be updated to
include the list of donors.

### How to add a new video post?

Add an entry in `data/videos.yaml` following the spec.
Then run the following script:

```sh
bb generate-video-posts
```

The script will automatically generate the article in the folder `content/post`
with the name `auto_generated__{$CREATION_DATE}_{$TITLE}`.

### How to add the bibliography sources for a video post?

1. create the following directory `scripts/data/video/source/{$youtube_id}`
   using the `youtube_id` of the video.
2. Create an `index.md` file that can contain markdown content for the
   bibliography section.
3. If assets are needed, create a folder
   `scripts/data/video/source/{$youtube_id}/bibliography/` and put
   them under this folder.
