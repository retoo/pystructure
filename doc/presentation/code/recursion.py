def recursive():
    if sometimes_true():
        return 1
    else:
        return recursive()

recursive() # Typ?