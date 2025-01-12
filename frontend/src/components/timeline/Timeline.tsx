import { ProgramStatus, TimelineState } from "../../common/types";
import { useStore } from "../../store/store";
import TimelineBar from "./TimelineBar";
import TimelinePoint from "./TimelinePoint";

function getTimelineState(relative: ProgramStatus, current: ProgramStatus | null): TimelineState {
  if (relative === current) {
    return TimelineState.Active;
  }

  if (!current || relative > current) {
    return TimelineState.Pending;
  }

  return TimelineState.Complete;
}

export default function ProgressBar() {
  const programStatus = useStore((state) => state.programStatus);

  if (programStatus === ProgramStatus.Failed) {
    return (
      <div className="flex justify-between items-center mb-7 mt-3 gap-1 w-[90%] ml-auto mr-auto">
        <div className="relative h-1 w-full bg-red-500 rounded-md shadow-[0px_0px_12px_0px_#ef4444]">
          <span className="absolute text-red-400 top-2 right-[50%] translate-x-[50%]">
            Something went wrong, try again.
          </span>
        </div>
      </div>
    );
  }

  return (
    <div className="flex justify-between items-center mb-6 mt-2 gap-1 w-[90%] ml-auto mr-auto">
      <TimelinePoint
        name={"Queue"}
        state={getTimelineState(ProgramStatus.Queueing, programStatus)}
      />
      <TimelineBar state={getTimelineState(ProgramStatus.Queueing, programStatus)} />
      <TimelinePoint
        name={"Build"}
        state={getTimelineState(ProgramStatus.Building, programStatus)}
      />
      <TimelineBar state={getTimelineState(ProgramStatus.Building, programStatus)} />
      <TimelinePoint name={"Run"} state={getTimelineState(ProgramStatus.Running, programStatus)} />
      <TimelineBar state={getTimelineState(ProgramStatus.Running, programStatus)} />
      <TimelinePoint
        name={"Done"}
        state={getTimelineState(ProgramStatus.Executed, programStatus)}
      />
    </div>
  );
}
