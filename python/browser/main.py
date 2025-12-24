import sys
import os

# browser klasorunu gorunur kilmak icin
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from browser import gogole

def menu():
    print("\n--- PYTHON ARAC KUTUSU ---")
    print("1. Google'i Hizli Ac")
    print("2. Cikis")
    
    secim = input("Seciminiz (1-2): ")
    
    if secim == "1":
        gogole.google_ac()
    elif secim == "2":
        print("Gorusmek uzere!")
    else:
        print("Gecersiz secim!")

if __name__ == "__main__":
    menu()
