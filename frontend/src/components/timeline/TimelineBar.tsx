import { TimelineState } from "../../common/types";

type Props = {
  state: TimelineState;
};
export default function TimelineBar({ state }: Props) {
  if (state === TimelineState.Pending) {
    return <div className="h-1 w-[170px] bg-light-gray rounded-md" />;
  }

  if (state === TimelineState.Complete) {
    return (
      <div className="h-1 w-[170px] bg-sky-500 rounded-md shadow-[0px_0px_12px_0px_#06b6d4]" />
    );
  }

  return (
    <div
      className="h-1 w-[170px] bg-sky-400 rounded-md bg-gradient-to-r from-light-gray via-sky-500 to-light-gray animate-processing-bar"
      style={{ backgroundSize: "500%" }}
    >
      <div
        className="absolute h-1 w-[170px] bg-sky-400 rounded-md bg-gradient-to-r from-light-gray to-sky-500 animate-processing-bar blur-md"
        style={{ backgroundSize: "500%" }}
      />
    </div>
  );
}
