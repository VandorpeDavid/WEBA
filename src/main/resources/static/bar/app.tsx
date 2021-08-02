const React = require('react');
const ReactDOM = require('react-dom');
import { BarComponent } from "./components/Bar";
import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import { locales } from "./locale";

const element = document.getElementById('bar-orders');
const barId = element.dataset.id;
const locale = element.dataset.locale;

console.log(locale, barId)
i18n
  .use(initReactI18next) // passes i18n down to react-i18next
  .init({
    // the translations
    // (tip move them in a JSON file and import them,
    // or even better, manage them via a UI: https://react.i18next.com/guides/multiple-translation-files#manage-your-translations-with-a-management-gui)
    resources: locales,
    lng: locale, // if you're using a language detector, do not define the lng option
    fallbackLng: "nl",
    interpolation: {
      escapeValue: false
    }
  });

ReactDOM.render(
    <BarComponent barId={barId}/>,
    element
);

