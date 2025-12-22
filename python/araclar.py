import webbrowser

# Bu dosya Java'dan bagimsizdir.
# Terminalden calistirilir.

def google_ac():
   # print("Google Chrome aciliyor...")
    webbrowser.open("https://www.google.com")

# Dosya dogrudan calistirildiginda burasi calisir
if __name__ == "__main__":
    google_ac()
