import os
import json
import logging
from datetime import datetime
from argparse import ArgumentParser

logging.basicConfig(level=logging.INFO)
parser = ArgumentParser()
parser.add_argument("-p", "--path", help="the old userdata storage path.")
parser.add_argument("-o", "--output", help="the output json file name.", default="audios.json")


def main():
    args = parser.parse_args()

    source_dir: str = args.path
    target: str = args.output
    if not (source_dir.endswith('oiduark') or source_dir.endswith('oiduark/')):
        source_dir = os.path.join(source_dir, 'oiduark')
    source_dir = os.path.join(source_dir, 'userdata')
    source = os.path.join(source_dir, 'audios.json')
    target = os.path.join(source_dir, target)
    if not os.path.exists(source):
        logging.error('source metadata file not found.')
        return

    logging.info('Converting the old metadata ...')
    #new_metadata = []
    new_metadata = dict()
    with open(source) as f:
        metadata = json.load(f)
        for filepath in metadata:
            audio_data = dict(source=filepath,
                              title=metadata[filepath].get('title', filepath.split('/')[-1]),
                              author=metadata[filepath].get('author', 'Unknown'),
                              duration=metadata[filepath].get('duration', 0.0),
                              favorite=metadata[filepath].get('favorite', False),
                              tags=metadata[filepath].get('tags', []),
                              cover=metadata[filepath].get('cover', 'Unknown'),
                              lyrics_file=metadata[filepath].get('lyrics_file', 'Unknown'),
                              last_modified=metadata[filepath].get('last_modified',
                                                                   datetime.now().strftime('%Y-%m-%d %H:%M:%S')))
            #new_metadata.append(audio_data)
            new_metadata[filepath] = audio_data

    logging.info(f'Writing to new file: {target}.')
    with open(target, encoding='utf-8', mode='w') as f:
        json.dump(new_metadata, f, ensure_ascii=False, indent=4)

    logging.info('Converted successfully.')


if __name__ == '__main__':
    main()
