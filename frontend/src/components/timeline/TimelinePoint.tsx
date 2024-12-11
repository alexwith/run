import { TimelineState } from "../../common/types";

type Props = {
  name: string;
  state: TimelineState;
};

export default function TimelinePoint({ name, state }: Props) {
  if (state === TimelineState.Pending) {
    return (
      <div className="relative w-3 h-3 bg-gray rounded-full">
        <span className="absolute text-gray text-center top-3 right-[50%] translate-x-[50%]">
          {name}
        </span>
      </div>
    );
  }

  return (
    <div className="relative w-3 h-3 bg-sky-400 rounded-full shadow-[0px_0px_12px_0px_#06b6d4]">
      <span className="absolute text-sky-400 text-center top-3 right-[50%] translate-x-[50%]">
        {name}
      </span>
    </div>
  );
}
