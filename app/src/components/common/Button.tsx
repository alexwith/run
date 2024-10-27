import { MouseEvent, ReactNode } from "react";

type Props = {
  name: string;
  icon?: ReactNode;
  onClick?: (event: MouseEvent) => void;
  onMouseEnter?: (event: MouseEvent) => void;
  onMouseLeave?: (event: MouseEvent) => void;
};

export default function Button({ name, icon, onClick, onMouseEnter, onMouseLeave }: Props) {
  return (
    <div
      className="flex items-center gap-1 bg-sky-400 text-sky-900 text-sm font-semibold w-fit px-2 py-1 rounded-md hover:cursor-pointer hover:bg-sky-500"
      onClick={onClick}
      onMouseEnter={onMouseEnter}
      onMouseLeave={onMouseLeave}
    >
      <span>{name}</span> {icon}
    </div>
  );
}
