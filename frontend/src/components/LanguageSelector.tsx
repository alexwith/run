import { useState } from "react";
import { CodeIcon } from "../common/icons";
import { Language, languages } from "../common/languages";
import { useStore } from "../store/store";
import Button from "./common/Button";

export default function LanguageSelector() {
  const [hovering, setHovering] = useState<boolean>(false);
  const [filter, setFilter] = useState<string>("");

  const language = useStore((state) => state.language);

  const setLanguage = useStore((actions) => actions.setLanguage);

  const handleLanguageClick = (language: Language) => {
    setLanguage(language);
    setHovering(false);
  };

  return (
    <div
      className="relative"
      onMouseEnter={() => setHovering(true)}
      onMouseLeave={() => setHovering(false)}
    >
      <Button name={language.name} icon={<CodeIcon size={14} />} />
      {hovering && (
        <div className="flex flex-col absolute z-10 bg-gray border-[1px] border-light-gray w-[150px] rounded-md right-0 py-1 px-2">
          <input
            className="text-sm appearance-none focus:outline-hidden bg-transparent mb-1 pb-1 border-b-[1px] border-light-gray"
            type="text"
            placeholder="Search language"
            onChange={(event) => setFilter(event.currentTarget.value.toLowerCase())}
          />
          <div className="flex flex-col">
            {[...languages.values()]
              .filter((language) => language.name.toLowerCase().includes(filter))
              .map((language, i) => (
                <p
                  key={i}
                  className="text-zinc-400 text-sm font-semibold hover:cursor-pointer hover:text-sky-400"
                  onClick={() => handleLanguageClick(language)}
                >
                  {language.name}
                </p>
              ))}
          </div>
        </div>
      )}
    </div>
  );
}
