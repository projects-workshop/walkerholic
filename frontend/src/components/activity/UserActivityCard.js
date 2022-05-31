import React, { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import basicProfile from '../../images/basicProfile.svg'
import { deleteUserActivity } from '../../_actions/ActivityActions'
import { LEVEL_DOWN } from '../../_constants/AuthConstants'
import EditUserActivity from './EditUserActivity'
import moment from 'moment'


function UserActivityCard({activity}) {

    const auth = useSelector(state => state.auth)
    const [isEdit, setIsEdit] = useState(false)

    

    const dispatch = useDispatch()

    const handleDelete = () =>{
        if(window.confirm("Are you sure to delete this activity?")){
            dispatch(deleteUserActivity(activity.id, activity.score, activity.finished)).then(res=>{
                if(auth.user.level !== res){
                    dispatch({
                        type:LEVEL_DOWN,
                        payload:res
                    })
                }
            })
        }
    }

    return (
        <>
        <div className="useractivity_card">
            <div className="useractivity_card_icon">
                {
                    !activity.finished &&
                    <i className="far fa-edit" onClick={()=>setIsEdit(true)}></i>
                }
                <i className="far fa-trash-alt" onClick={handleDelete}></i>
            </div>
            <div className="useractivity_card_info">
                {
                    activity.finished
                    ? <div className="useractivity_card_status" style={{color:"#61b063"}}>FINISHED</div>
                    : <div className="useractivity_card_status" style={{color:"#e35d3b"}}>ON GOING</div>
                }
                {
                    activity.finished &&
                    <div className="useractivity_card_time">
                        {moment(activity.updatedAt).format('L')}
                    </div>
                }
                <div className="useractivity_card_image">
                    <img src={activity.activityImageUrl? activity.activityImageUrl : basicProfile} alt="" />
                </div>
                <div className="useractivity_card_name">{activity.activityName}</div>
                <div className="useractivity_card_score">{activity.score} points</div>
            </div>


        </div>
        {
                isEdit &&
                <EditUserActivity activity={activity} setIsEdit={setIsEdit} isEdit={isEdit} />
            }
        </>
    )
}

export default UserActivityCard
