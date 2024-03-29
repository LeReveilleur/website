# website

Hugo website for Le Réveilleur

## Deploy

### Netlify

Deployment is done via [Netlify](https://www.netlify.com) that is configured to
trigger a build when new code is pushed into the main branch.

One can find the netlify config file [here](./netlify.toml).

### DNS setup

To setup the DNS, it is done through Netlify directly.

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

#### Git usage

Use the following git commands to stage your changes.

To pull the latest changes from the remote repository:

```sh
git pull origin
```

To list all the local changes:

```sh
git status
```

To add all the changes to the index:

```sh
git add .
```

To create a commit with a message:

```sh
git commit -m "Ajout de la bibliographie de la vidéo numéro 42"
```

To push to the remote repository

```sh
git push origin
```

### How to set a specific URL for a blog post?

In the file `data/videos.yaml`, add an entry with the key `url` andthe
value `/le-charbon-et-ses-impacts`.

### How to add the bibliography sources for a video post?

1. Create the following directory `scripts/data/video/source/{$youtube_id}__{title_of_the_video_without_spaces_but_with_underscores}`
   using the `youtube_id` of the video and a prefix for the title.
2. Create an `index.md` file that can contain markdown content for the
   bibliography section.
3. If assets are needed, create a folder
   `scripts/data/video/source/{$youtube_id}__{title_of_the_video_without_spaces_but_with_underscores}/bibliography/`
and put them under this folder.

### How to change the cover image of a video?

1. Open the `data/videos.yaml` file to retrieve its `youtube_id`. Eg. `eruVCRrIkCY`
2. Open the folder `assets/img/video_thumbnail/`
3. Replace the thumbnail called `{$youtube_id}.jpg` with the image you want to use.
4. Run the script: `bb generate-video-posts`
