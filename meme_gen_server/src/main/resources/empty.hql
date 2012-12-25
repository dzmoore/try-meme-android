--from Meme m where m.lvMemeType.id = 1 and m.isSampleMeme = true
from Meme m where m.isSampleMeme = true and m.lvMemeType.active = true and m.lvMemeType.id = 1