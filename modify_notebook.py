import json

with open('01-python/ingestion.ipynb', 'r', encoding='utf-8') as f:
    data = json.load(f)

# Get the last cell (production cell)
last_cell = data['cells'][-1]
source = last_cell['source']

# Add comments before the code in the last cell
# Find the first line that contains code (not a comment)
first_code_idx = None
for i, line in enumerate(source):
    if line.strip() and not line.strip().startswith('#'):
        first_code_idx = i
        break

if first_code_idx is not None:
    # Insert comments before the first code line
    comment_lines = [
        '# Cette cellule génère le fichier contract 1 (`courses_propres.csv`) destiné au maillon Java.\n',
        '# Elle lit le fichier brut `resultats.csv`, applique les transformations demandées\n',
        '# (conversion des temps en secondes, gestion des abandons) et écrit le fichier propre.\n'
    ]
    # Insert after the header comment (index 0)
    new_source = source[:1] + comment_lines + source[1:]
    last_cell['source'] = new_source

# Write back
with open('01-python/ingestion.ipynb', 'w', encoding='utf-8') as f:
    json.dump(data, f, indent=1, ensure_ascii=False)
print('Modified Python notebook successfully')