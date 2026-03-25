import csv

def process_data(file_path):
    # This is a legacy function for processing CSV files.
    # Vague prompt scenario: "Make this code better."
    results = []
    with open(file_path, mode='r') as file:
        reader = csv.DictReader(file)
        for row in reader:
            # Simulate some processing
            if row['status'] == 'completed':
                results.append(row)
    return results
