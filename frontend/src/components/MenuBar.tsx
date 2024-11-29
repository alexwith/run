import { SettingsIcon } from "../common/icons";
import Button from "./common/Button";
import LanguageSelector from "./LanguageSelector";
import RunButton from "./RunButton";

export default function MenuBar() {
  return (
    <div className="flex justify-between">
      <RunButton />
      <div className="flex gap-2">
        <LanguageSelector />
        <Button icon={<SettingsIcon size={20} />} />
      </div>
    </div>
  );
}
