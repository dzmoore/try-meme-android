from memegendb import *

bg_names = get_meme_background_names()
print("Found " + str(len(bg_names)) + " backgrounds")

for ea_bg_name in bg_names:
    print("Processing " + ea_bg_name)

    bg_id = get_meme_background_id_for_name(ea_bg_name)
    print("Found id " + str(bg_id) + " for " + ea_bg_name)
   
    samples = get_samples(ea_bg_name, 3)
    print("Found " + str(len(samples)) + " samples for " + ea_bg_name)

    sample_meme_ids = []
    for ea_sample in samples:
        print("Creating meme for sample: " + str(ea_sample))
        meme_id = create_meme(
            ea_sample[1],
            ea_sample[2],
            bg_id
        )
    
        print("Created meme with id " + str(meme_id))
        sample_meme_ids.append(meme_id)

    print("Done creating samples for background: " + ea_bg_name)
    print("Storing samples for background: " + ea_bg_name)

    store_samples(bg_id, sample_meme_ids)

    print("Done with sample creation for background: " + ea_bg_name)


