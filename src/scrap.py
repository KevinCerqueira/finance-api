from bs4 import BeautifulSoup
import requests
import re


class Scrap:
    def request(self, url: str = "", deep_request: bool = False) -> BeautifulSoup | None:
        try:
            response = requests.get(url)
            soup = BeautifulSoup(response.text, "html.parser")
            if deep_request and "window.location.replace" in response.text:
                soup = BeautifulSoup(response.text, "html.parser")
                script = soup.find('script', string=re.compile("window.location.replace"))
                new_url = re.search(r'window.location.replace\("(.*?)"\)', script.string).group(1)
                response = requests.get(new_url)
                soup = BeautifulSoup(response.text, "html.parser")

            return soup
        except Exception as e:
            self.log.error(str(e))
            return None

    def get_finance(self, name: str) -> dict | None:
        url = f'https://www.google.com/finance/quote/{name}'
        print("\n\nREQUEST-URL: ", url)
        response = self.request(url)

        div_value = response.find("div", {"class": "YMlKec fxKbKc"})
        value = None
        if div_value is not None:
            value = (div_value.text.replace(",", "")).replace("R$", "")

        div_about = response.find("div", {"class": "bLLb2d"})
        about = None
        if div_about is not None:
            about = div_about.text
        else:
            div_about = response.find("span", {"class": "w4txWc oJeWuf"})
            if div_about is not None:
                about = div_about.text

        div_name = response.find("div", {"class": "zzDege"})
        print(div_name)
        name = None
        if div_name is not None:
            name = div_name.text

        return {
            "name": name,
            "value": float(value),
            "about": about
        }


if __name__ == "__main__":
    s = Scrap()
    soup = s.request("https://www.google.com/finance/quote/BTC-BRL")
    text = soup.find("div", {"class": "YMlKec fxKbKc"})
    print(float(text.text.replace(",", "")))
